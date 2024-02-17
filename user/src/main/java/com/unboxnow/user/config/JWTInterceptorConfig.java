package com.unboxnow.user.config;

import com.unboxnow.user.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JWTInterceptorConfig implements WebMvcConfigurer {

    private final JWTInterceptor jwtInterceptor;

    @Autowired
    public JWTInterceptorConfig(JWTInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/members/login",
                        "/members/register",
                        "/members/forget-password/*",
                        "/members/reset-password/*");
    }
}
