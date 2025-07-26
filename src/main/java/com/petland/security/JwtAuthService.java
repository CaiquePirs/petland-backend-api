package com.petland.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthService {

    private final String SECRET_KEY;

    public JwtAuthService(@Value("${SECRET_KEY}") String SECRET_KEY){
        this.SECRET_KEY = SECRET_KEY;
    }

    public DecodedJWT validateToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        try {
            return JWT.require(algorithm)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
