package com.hrms.backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String email, UUID role) throws
            IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(email)
                .withClaim("role", Collections.singletonList(role))
                .withIssuedAt(new Date())
                .withIssuer("HRMS")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateToken(String email, String token) throws
            JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(email)
                .withIssuer("HMRS")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("role").asString();
    }
}