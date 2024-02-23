package com.unboxnow.common.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.unboxnow.common.constant.Token;
import com.unboxnow.common.exception.TokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JWTProvider {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public JWTProvider(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateAccessToken(int memberId, List<String> roles) {
        JWTCreator.Builder builder = JWT.create();
        builder.withAudience(String.valueOf(memberId));
        builder.withClaim("roles", roles);
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(Token.ACCESS.getTokenExpiry());
        builder.withExpiresAt(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()));
        String token = builder.sign(Algorithm.HMAC256(Token.ACCESS.getSecret()));
        writeToRedis(token, memberId, Token.ACCESS);
        return token;
    }

    public String generateRefreshToken(int memberId) {
        String token = getToken(memberId, Token.REFRESH);
        writeToRedis(token, memberId, Token.REFRESH);
        return token;
    }

    public String generateResetToken(int memberId) {
        String token = getToken(memberId, Token.RESET);
        writeToRedis(token, memberId, Token.RESET);
        return token;
    }

    private String getToken(int memberId, Token tokenType) {
        JWTCreator.Builder builder = JWT.create();
        builder.withAudience(String.valueOf(memberId));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(tokenType.getTokenExpiry());
        builder.withExpiresAt(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()));
        return builder.sign(Algorithm.HMAC256(tokenType.getSecret()));
    }

    private void writeToRedis(String token, int memberId, Token tokenType) {
        String key = tokenType.getRedisKey(memberId);
        redisTemplate.opsForValue().set(key, token);
        redisTemplate.expire(key, tokenType.getRedisExpiry(), TimeUnit.MINUTES);
    }

    public String updateAccessToken(String oldToken) {
        int memberId = getMemberId(oldToken, Token.ACCESS);
        List<String> roles = getRolesByToken(oldToken);
        return generateAccessToken(memberId, roles);
    }

    private String fetchToken(int memberId, Token tokenType) {
        String token = redisTemplate.opsForValue().get(tokenType.getRedisKey(memberId));
        if (token == null || token.isEmpty()) {
            throw new TokenException(tokenType, "fail to fetch token on Redis");
        }
        return token;
    }

    private void fetchAndValidate(String token, int memberId, Token tokenType) {
        exists(token, tokenType);
        String tokenOnRedis = fetchToken(memberId, tokenType);
        if (!token.equals(tokenOnRedis)) {
            throw new TokenException(tokenType, "fail to validate token on Redis");
        }
    }

    public Map<String, String> verifyAccessAndRefreshTokens(String accessToken, String refreshToken, int memberId) {
        fetchAndValidate(accessToken, memberId, Token.ACCESS);
        fetchAndValidate(refreshToken, memberId, Token.REFRESH);
        try {
            JWT.require(Algorithm.HMAC256(Token.ACCESS.getSecret())).build().verify(accessToken);
        } catch (TokenExpiredException accessEx) {
            try {
                JWT.require(Algorithm.HMAC256(Token.REFRESH.getSecret())).build().verify(refreshToken);
            } catch (TokenExpiredException refreshEx) {
                throw new TokenException(Token.REFRESH, "expired");
            } catch (JWTVerificationException ex) {
                throw new TokenException(Token.REFRESH, "fail to validate");
            }
        } catch (JWTVerificationException ex) {
            throw new TokenException(Token.ACCESS, "fail to validate");
        }
        Map<String, String> tokens = new HashMap<>();
        tokens.put(Token.ACCESS.getHeaderKey(), updateAccessToken(accessToken));
        tokens.put(Token.REFRESH.getHeaderKey(), generateRefreshToken(memberId));
        return tokens;
    }

    public void verifyResetToken(String resetToken, int memberId) {
        fetchAndValidate(resetToken, memberId, Token.RESET);
        try {
            JWT.require(Algorithm.HMAC256(Token.RESET.getSecret())).build().verify(resetToken);
        } catch (TokenExpiredException ex) {
            throw new TokenException(Token.RESET, "expired");
        } catch (JWTVerificationException ex) {
            throw new TokenException(Token.RESET, "fail to validate");
        }
    }

    public static List<String> getRolesByToken(String accessToken) {
        exists(accessToken, Token.ACCESS);
        List<String> roles;
        try {
            roles = JWT.decode(accessToken).getClaims().get("roles").asList(String.class);
        } catch (JWTDecodeException ex) {
            throw new TokenException(Token.ACCESS, "fail to decode");
        }
        return roles;
    }

    public List<String> getRolesById(int memberId) {
        return getRolesByToken(fetchToken(memberId, Token.ACCESS));
    }

    private static void exists(String token, Token tokenType) {
        if (token == null || token.isEmpty()) {
            throw new TokenException(tokenType, "null or empty");
        }
    }

    public static int getMemberId(String accessToken, String refreshToken) {
        int accessMemberId = getMemberId(accessToken, Token.ACCESS);
        int refreshMemberId = getMemberId(refreshToken,Token.REFRESH);
        if (accessMemberId != refreshMemberId) {
            throw new TokenException();
        }
        return accessMemberId;
    }

    public static int getMemberId(String token, Token tokenType) {
        exists(token, tokenType);
        try {
            return Integer.parseInt(JWT.decode(token).getAudience().get(0));
        } catch (JWTDecodeException ex) {
            throw new TokenException(tokenType, "fail to decode");
        }
    }

    public void clearAccessAndRefreshTokens(int memberId) {
        redisTemplate.delete(Token.ACCESS.getRedisKey(memberId));
        redisTemplate.delete(Token.REFRESH.getRedisKey(memberId));
    }

    public void clearAllTokens(int memberId) {
        clearAccessAndRefreshTokens(memberId);
        redisTemplate.delete(Token.RESET.getRedisKey(memberId));
    }
}
