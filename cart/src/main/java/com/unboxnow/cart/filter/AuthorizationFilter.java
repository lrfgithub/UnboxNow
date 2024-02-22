package com.unboxnow.cart.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.cart.constant.Authorization;
import com.unboxnow.cart.dto.CartItemDTO;
import com.unboxnow.common.component.JWTProvider;
import com.unboxnow.common.constant.Token;
import com.unboxnow.common.wrapper.CustomRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(urlPatterns = {"/carts/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CustomRequestWrapper wrapper = new CustomRequestWrapper(request);
        String method = wrapper.getMethod();
        String url = String.valueOf(wrapper.getRequestURL());
        Authorization authorization = Authorization.getAuthorization(method, url);
        if (Authorization.isCalibrationRequired(authorization)) {
            String token = wrapper.getHeader(Token.ACCESS.getHeaderKey());
            JWTProvider.exists(token, Token.ACCESS);
            int memberId = JWTProvider.getMemberId(token, Token.ACCESS);
            String body = wrapper.getBodyString();
            ObjectMapper mapper = new ObjectMapper();
            CartItemDTO dto = mapper.readValue(body, CartItemDTO.class);
            dto.setMemberId(memberId);
            wrapper = new CustomRequestWrapper(wrapper, mapper.writeValueAsBytes(dto));
        }
        filterChain.doFilter(wrapper, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
