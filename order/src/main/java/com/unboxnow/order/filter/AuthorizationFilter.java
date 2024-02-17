package com.unboxnow.order.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.common.component.JWTProvider;
import com.unboxnow.common.constant.Token;
import com.unboxnow.common.wrapper.CustomRequestWrapper;
import com.unboxnow.order.constant.Authorization;
import com.unboxnow.order.dto.MemberAddressDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = {"/orders/*"})
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
            List<String> roles = JWTProvider.getRolesByToken(token);
            boolean isCustomer = Authorization.Role.isCustomer(roles);
            JWTProvider.exists(token, Token.ACCESS);
            if (Authorization.getCalibrationCode(authorization, isCustomer) == 1) {
                int memberId = JWTProvider.getMemberId(token);
                String body = wrapper.getBodyString();
                ObjectMapper mapper = new ObjectMapper();
                MemberAddressDTO dto = mapper.readValue(body, MemberAddressDTO.class);
                dto.setMemberId(memberId);
                wrapper = new CustomRequestWrapper(wrapper, mapper.writeValueAsBytes(dto));
            }
        }
        filterChain.doFilter(wrapper, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
