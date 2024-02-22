package com.unboxnow.user.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.common.component.JWTProvider;
import com.unboxnow.common.constant.Token;
import com.unboxnow.common.wrapper.CustomRequestWrapper;
import com.unboxnow.user.constant.Authorization;
import com.unboxnow.user.dto.AddressDTO;
import com.unboxnow.user.dto.MemberDTO;
import com.unboxnow.user.dto.PasswordForm;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = {"/user-addresses/*", "/members/*"})
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
            String token;
            boolean isCustomer = false;
            int memberId;
            if (authorization == Authorization.RESET_PASSWORD) {
                token = Authorization.getValueFromUrl(authorization, url);
                JWTProvider.exists(token, Token.RESET);
                memberId = JWTProvider.getMemberId(token, Token.RESET);
            } else {
                token = wrapper.getHeader(Token.ACCESS.getHeaderKey());
                List<String> roles = JWTProvider.getRolesByToken(token);
                isCustomer = Authorization.Role.isCustomer(roles);
                JWTProvider.exists(token, Token.ACCESS);
                memberId = JWTProvider.getMemberId(token, Token.ACCESS);
            }
            String body = wrapper.getBodyString();
            ObjectMapper mapper = new ObjectMapper();
            switch (Authorization.getCalibrationCode(authorization, isCustomer)) {
                case 1 -> {
                    PasswordForm form = mapper.readValue(body, PasswordForm.class);
                    form.setMemberId(memberId);
                    wrapper = new CustomRequestWrapper(wrapper, mapper.writeValueAsBytes(form));
                }
                case 2 -> {
                    MemberDTO dto = mapper.readValue(body, MemberDTO.class);
                    dto.setId(memberId);
                    wrapper = new CustomRequestWrapper(wrapper, mapper.writeValueAsBytes(dto));
                }
                case 3 -> {
                    AddressDTO dto = mapper.readValue(body, AddressDTO.class);
                    dto.setMemberId(memberId);
                    wrapper = new CustomRequestWrapper(wrapper, mapper.writeValueAsBytes(dto));
                }
            }
        }
        filterChain.doFilter(wrapper, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
