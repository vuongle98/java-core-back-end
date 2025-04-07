package com.vuog.core.config.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.error("Unauthorized error: {}", exception.getMessage());
        log.error(request.getRequestURI());
        System.out.println(request.getRequestURI());
        System.out.println(request.getMethod());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
    }
}