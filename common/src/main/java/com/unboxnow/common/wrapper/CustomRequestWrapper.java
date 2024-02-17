package com.unboxnow.common.wrapper;

import com.unboxnow.common.exception.CustomHttpRequestException;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CustomRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> params = new HashMap<>();

    private final byte[] body;

    public CustomRequestWrapper(HttpServletRequest request) {
        super(request);
        this.body = getBodyString(request).getBytes(StandardCharsets.UTF_8);
        this.params.putAll(request.getParameterMap());
    }

    public CustomRequestWrapper(HttpServletRequest request, byte[] body) {
        super(request);
        this.body = body;
        this.params.putAll(request.getParameterMap());
    }

    @Override
    public ServletInputStream getInputStream(){
        final ByteArrayInputStream bias = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return bias.read();
            }
        };
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return params;
    }

    public String getBodyString() {
        return new String(body);
    }

    public static String getBodyString(ServletRequest request) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            char[] buffer = new char[8192];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, charsRead);
            }
        } catch (IOException ex) {
            throw new CustomHttpRequestException(ex.getClass().toString(), ex.getMessage());
        }
        return builder.toString();
    }
}
