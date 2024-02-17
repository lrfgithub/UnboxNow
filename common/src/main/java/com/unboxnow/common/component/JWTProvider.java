package com.unboxnow.common.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.unboxnow.common.constant.Token;
import com.unboxnow.common.exception.TokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JWTProvider {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public JWTProvider(RedisTemplate<String, String> stringRedisTemplate) {
        this.redisTemplate = stringRedisTemplate;
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
        DecodedJWT decoded = JWT.decode(oldToken);
        int memberId = Integer.parseInt(decoded.getAudience().get(0));
        List<String> roles = getRolesByToken(oldToken);
        return generateAccessToken(memberId, roles);
    }

    private static void verifyToken(String token, Token tokenType) {
        switch (tokenType) {
            case ACCESS -> JWT.require(Algorithm.HMAC256(Token.ACCESS.getSecret())).build().verify(token);
            case REFRESH -> JWT.require(Algorithm.HMAC256(Token.REFRESH.getSecret())).build().verify(token);
            case RESET -> JWT.require(Algorithm.HMAC256(Token.RESET.getSecret())).build().verify(token);
        }
    }

    public String getTokenFromRedis(int memberId, Token tokenType) {
        return redisTemplate.opsForValue().get(tokenType.getRedisKey(memberId));
    }

    public void verifyAccessAndRefreshTokens(String accessToken, String refreshToken, int memberId) {
        if (!accessToken.equals(getTokenFromRedis(memberId, Token.ACCESS))) {
            throw new TokenException("fail to verify " + Token.ACCESS.getHeaderKey());
        }
        if (!refreshToken.equals(getTokenFromRedis(memberId, Token.REFRESH))) {
            throw new TokenException("fail to verify " + Token.REFRESH.getHeaderKey());
        }
        try {
            verifyToken(accessToken, Token.ACCESS);
        } catch (TokenExpiredException accessEx) {
            try {
                verifyToken(refreshToken, Token.REFRESH);
                accessToken = updateAccessToken(accessToken);
                redisTemplate.opsForValue().set(Token.ACCESS.getRedisKey(memberId), accessToken);
                redisTemplate.expire(Token.ACCESS.getRedisKey(memberId),
                        Token.ACCESS.getRedisExpiry(),
                        TimeUnit.MINUTES);
                refreshToken = generateRefreshToken(memberId);
                redisTemplate.opsForValue().set(Token.REFRESH.getRedisKey(memberId), refreshToken);
                redisTemplate.expire(Token.REFRESH.getRedisKey(memberId),
                        Token.REFRESH.getRedisExpiry(),
                        TimeUnit.MINUTES);
            } catch (TokenExpiredException refreshEx) {
                throw new TokenException("expired");
            } catch (JWTVerificationException ex) {
                throw new TokenException("fail to verify " + Token.REFRESH.getHeaderKey());
            }
        } catch (JWTVerificationException ex) {
            throw new TokenException("fail to verify " + Token.ACCESS.getHeaderKey());
        }
    }

    public void verifyAccessAndRefreshTokens(String accessToken, String refreshToken) {
        int memberId = getMemberId(accessToken, refreshToken);
        verifyAccessAndRefreshTokens(accessToken, refreshToken, memberId);
    }

    public void verifyResetToken(String resetToken, int memberId) {
        if (!resetToken.equals(getTokenFromRedis(memberId, Token.RESET))) {
            throw new TokenException("fail to verify " + Token.RESET.getHeaderKey());
        }
        try {
            verifyToken(resetToken, Token.RESET);
        } catch (TokenExpiredException ex) {
            throw new TokenException("expired");
        } catch (JWTVerificationException ex) {
            throw new TokenException("fail to verify " + Token.RESET.getHeaderKey());
        }
    }

    public static List<String> getRolesByToken(String accessToken) {
        List<String> roles;
        try {
            roles = JWT.decode(accessToken).getClaims().get("roles").asList(String.class);
        } catch (JWTDecodeException ex) {
            throw new TokenException("fail to decode");
        }
        return roles;
    }

    public List<String> getRolesById(int memberId) {
        String token = getTokenFromRedis(memberId, Token.ACCESS);
        if (token == null) {
            throw new TokenException("expired");
        }
        return getRolesByToken(token);
    }

    public static void exist(String accessToken, String refreshToken) {
        exists(accessToken, Token.ACCESS);
        exists(refreshToken, Token.REFRESH);
    }

    public static void exists(String token, Token tokenType) {
        if (token == null || token.isEmpty()) {
            throw new TokenException(tokenType.getHeaderKey() + " not found");
        }
    }

    public static int getMemberId(String accessToken, String refreshToken) {
        int memberId;
        try {
            int accessMemberId = Integer.parseInt(JWT.decode(accessToken).getAudience().get(0));
            int refreshMemberId = Integer.parseInt(JWT.decode(refreshToken).getAudience().get(0));
            if (accessMemberId != refreshMemberId) {
                throw new TokenException("fail to verify memberId");
            } else {
                memberId = accessMemberId;
            }
        } catch (JWTDecodeException ex) {
            throw new TokenException("fail to decode");
        }
        return memberId;
    }

    public static int getMemberId(String token) {
        int memberId;
        try {
            memberId = Integer.parseInt(JWT.decode(token).getAudience().get(0));
        } catch (JWTDecodeException ex) {
            throw new TokenException("fail to decode");
        }
        return memberId;
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
