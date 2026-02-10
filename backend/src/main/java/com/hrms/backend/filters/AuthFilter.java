package com.hrms.backend.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (req.getRequestURI().startsWith("/api/auth")
                || req.getRequestURI().startsWith("swagger")
                || req.getRequestURI().startsWith("/swagger-ui")
                || req.getRequestURI().startsWith("/v3/api-docs")
                || req.getRequestURI().startsWith("/swagger-resources")
                || req.getRequestURI().startsWith("/webjars")) {
            filterChain.doFilter(req, res);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.equals("Bearer something")) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType("application/json");
            res.getWriter().write("""
                    {
                        "message": "Unauthorized",
                        "data": null
                    }
                    """);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("dummy", null, List.of()));
        filterChain.doFilter(req, res);
    }
}
