package com.example.learnJava.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import com.example.learnJava.domain.Permissions;
import com.example.learnJava.domain.Roles;
import com.example.learnJava.domain.User;
import com.example.learnJava.service.UserService;
import com.example.learnJava.utils.SecurityUtils;
import com.example.learnJava.utils.error.PermissionInterceptorException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Transactional
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        // System.out.println(">>> RUN preHandle");
        // System.out.println(">>> path= " + path);
        // System.out.println(">>> httpMethod= " + httpMethod);
        // System.out.println(">>> requestURI= " + requestURI);

        String email = SecurityUtils.getCurrentUserLogin().isPresent() == true
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";
        if (email != null && !email.isEmpty()) {
            User user = this.userService.handleByUser(email);
            if (user != null) {
                Roles roles = user.getRole();
                if (roles != null) {
                    List<Permissions> permissions = roles.getPermissions();
                    Boolean isAllow = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
                            && item.getMethod().equals(httpMethod));

                    if (isAllow == false) {
                        throw new PermissionInterceptorException("Bạn không có quyền truy cập vào API này");
                    }
                } else {
                    throw new PermissionInterceptorException("Bạn không có quyền truy cập vào API này");
                }
            }
        }
        return true;
    }
}
