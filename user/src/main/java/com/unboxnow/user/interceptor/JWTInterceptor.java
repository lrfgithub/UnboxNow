package com.unboxnow.user.interceptor;

import com.unboxnow.common.component.JWTProvider;
import com.unboxnow.common.constant.Token;
import com.unboxnow.common.exception.NotAuthorizedException;
import com.unboxnow.user.constant.Authorization;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Map;

@Component
@ComponentScan(basePackages = {"com.unboxnow.common.component"})
public class JWTInterceptor implements HandlerInterceptor {

    private final JWTProvider jwtProvider;

    @Autowired
    public JWTInterceptor(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        String accessToken = request.getHeader(Token.ACCESS.getHeaderKey());
        String refreshToken = request.getHeader(Token.REFRESH.getHeaderKey());
        int memberId = JWTProvider.getMemberId(accessToken, refreshToken);
        Map<String, String> tokens = jwtProvider.verifyAccessAndRefreshTokens(accessToken, refreshToken, memberId);
        accessToken = tokens.get(Token.ACCESS.getHeaderKey());
        refreshToken = tokens.get(Token.REFRESH.getHeaderKey());
        String method = request.getMethod();
        String url = String.valueOf(request.getRequestURL());
        List<String> roles = JWTProvider.getRolesByToken(accessToken);
        if (!Authorization.isAuthorized(method, url, memberId, roles)) {
            throw new NotAuthorizedException(memberId);
        }
        response.addHeader(Token.ACCESS.getHeaderKey(), accessToken);
        response.addHeader(Token.REFRESH.getHeaderKey(), refreshToken);
        return true;
    }
}
