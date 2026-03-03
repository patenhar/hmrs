package com.hrms.backend.filters;

import com.hrms.backend.services.JwtService;
import com.hrms.backend.utils.UserInfo;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthFilter(JwtService jwtService, @Lazy UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            @NonNull HttpServletResponse res,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        boolean isPublicPath = req.getRequestURI().equals("/api/auth/login")
                || req.getRequestURI().contains("swagger")
                || req.getRequestURI().contains("/v3/api-docs")
                || req.getRequestURI().contains("actuator");

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (isPublicPath) {
                filterChain.doFilter(req, res);
            } else {
                writeUnauthorized(res);
            }
            return;
        }

        try {
            String token = authHeader.substring(7);
            String email = jwtService.getEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserInfo userInfo = (UserInfo) userDetailsService.loadUserByUsername(email);
                if (jwtService.validateToken(token, userInfo.getUsername())) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    writeUnauthorized(res);
                    return;
                }
            }
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException | ClassCastException ex) {
            SecurityContextHolder.clearContext();
            writeUnauthorized(res);
            return;
        }

        filterChain.doFilter(req, res);
    }

    private void writeUnauthorized(HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType("application/json");
        res.getWriter().write("""
                {
                    "message": "Unauthorized",
                    "data": null
                }
                """);
    }
}
