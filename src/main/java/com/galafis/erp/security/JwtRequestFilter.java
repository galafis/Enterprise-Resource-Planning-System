package com.galafis.erp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT request filter that processes JWT tokens in HTTP requests.
 * Validates JWT tokens and sets authentication context.
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0
 * @since 2025-09-25
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Filters incoming requests to validate JWT tokens.
     * 
     * @param request HTTP request
     * @param response HTTP response
     * @param filterChain filter chain
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtTokenUtil.validateJwtToken(jwt)) {
                String username = jwtTokenUtil.getUsernameFromJwtToken(jwt);
                
                // For simplicity, we'll create basic authorities
                // In a real application, you'd fetch user details from the database
                List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_USER")
                );

                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                logger.debug("User '{}' authenticated successfully", username);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Parses JWT token from Authorization header.
     * 
     * @param request HTTP request
     * @return JWT token or null if not found
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);
        
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_PREFIX)) {
            return headerAuth.substring(BEARER_PREFIX.length());
        }
        
        return null;
    }
}
