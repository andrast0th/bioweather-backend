package com.example.bioweatherbackend.config.sec;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

public class RequireBasicAuthRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (handler instanceof HandlerMethod method) {
            return method.hasMethodAnnotation(RequireAuth.class)
                || method.getBeanType().isAnnotationPresent(RequireAuth.class);
        }
        return false;
    }
}