package com.unboxnow.cart.interceptor;

import com.unboxnow.cart.constant.Authorization;
import com.unboxnow.common.component.JWTProvider;
import com.unboxnow.common.constant.Token;
import com.unboxnow.common.exception.LoginException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

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
        JWTProvider.exist(accessToken, refreshToken);
        int memberId = JWTProvider.getMemberId(accessToken, refreshToken);
        jwtProvider.verifyAccessAndRefreshTokens(accessToken, refreshToken, memberId);
        String method = request.getMethod();
        String url = String.valueOf(request.getRequestURL());
        List<String> roles = JWTProvider.getRolesByToken(accessToken);
        if (!Authorization.isAuthorized(method, url, memberId, roles)) {
            throw new LoginException(memberId, "not authorized");
        }
        response.addHeader(Token.ACCESS.getHeaderKey(), accessToken);
        response.addHeader(Token.REFRESH.getHeaderKey(), refreshToken);
        return true;
    }
}
