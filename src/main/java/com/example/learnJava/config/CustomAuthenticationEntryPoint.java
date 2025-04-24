package com.example.learnJava.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.learnJava.domain.response.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Xóa dòng delegate.commence để không ghi response trước
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        restResponse.setMessage("Token không hợp lệ");

        // Kiểm tra lỗi để tránh NullPointerException
        restResponse.setError(authException.getMessage());

        // Ghi phản hồi JSON
        objectMapper.writeValue(response.getWriter(), restResponse);
    }
}
