package com.spring_boot_starter.api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Handle Exception with GlobalExceptionHandler not By Spring Security
        HandlerExceptionResolver resolver = (HandlerExceptionResolver) request.getAttribute("handlerExceptionResolver");
        if (resolver != null) {
            resolver.resolveException(request, response, null, authException);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
}
