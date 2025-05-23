package com.example.learnJava.utils;

import com.example.learnJava.domain.response.RestResponse;
import com.example.learnJava.utils.annotation.ApiMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FomatRestResponse implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        RestResponse<Object> restResponse = new RestResponse<>();
        ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);

        if (body instanceof String) {
            if (body instanceof String) {
                restResponse.setStatus(HttpStatus.OK.value());
                restResponse.setMessage(message != null ? message.value() : " Call API Success");
                restResponse.setData(body);
                restResponse.setError(null);

                try {
                    return objectMapper.writeValueAsString(restResponse); // Chuyển thành JSON String
                } catch (Exception e) {
                    throw new RuntimeException("Error converting response to JSON", e);
                }
            }
        }

        String path = request.getURI().getPath();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return body;
        }

        if (body instanceof Resource) {
            return body;
        }
        int status;
        if (response instanceof ServletServerHttpResponse servletResponse) {
            status = servletResponse.getServletResponse().getStatus();
        } else {
            status = HttpStatus.OK.value(); // Mặc định nếu không lấy được status
        }

        restResponse.setStatus(status);

        if (status >= 400) {
            return body;
        } else {
            restResponse.setMessage(message != null ? message.value() : " Call API Success");
            restResponse.setData(body);
            restResponse.setError(null);
        }
        return restResponse;
    }
}
