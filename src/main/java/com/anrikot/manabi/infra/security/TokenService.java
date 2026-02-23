package com.anrikot.manabi.infra.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.anrikot.manabi.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {
    @Value("api.security.token.secret")
    private String SECRET;
    private final String MANABI = "manabi-api";

    public String generateToken(User user) {
        try {
            Algorithm alg = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                .withIssuer(MANABI)
                .withSubject(user.getUsername())
                .withExpiresAt(genExpirationDate())
                .sign(alg);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm alg = Algorithm.HMAC256(SECRET);
            return JWT.require(alg)
                .withIssuer(MANABI)
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    public Instant genExpirationDate() {
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }
}
