package com.galafis.erp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT authentication entry point that handles unauthorized access attempts.
 * Returns a JSON error response when authentication fails.
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0
 * @since 2025-09-25
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handles authentication exceptions by returning a JSON error response.
     * 
     * @param request HTTP request
     * @param response HTTP response
     * @param authException authentication exception
     * @throws IOException if I/O error occurs
     * @throws ServletException if servlet error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "Full authentication is required to access this resource");
        body.put("path", request.getServletPath());
        body.put("timestamp", System.currentTimeMillis());

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
