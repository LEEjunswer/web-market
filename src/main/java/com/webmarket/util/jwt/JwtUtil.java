package com.webmarket.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtil {

    private static String secretKey;

    public JwtUtil(@Value("${jwt.secret_key}") String key) {
        secretKey = key;
    }

    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 15; // 15분
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7일



    public static String generateAccessToken(String memberId, String role) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(memberId)
                .withIssuer("auth0")
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .sign(algorithm);
    }

    public static String generateRefreshToken(String memberId) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(memberId)
                .withIssuer("auth0")
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .sign(algorithm);
    }

    public static DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public static String getMemberIdFromToken(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return jwt != null ? jwt.getSubject() : null;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public static String getRoleFromToken(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return jwt != null ? jwt.getClaim("role").asString() : null;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public static Date getExpirationDate(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getExpiresAt() : null;
    }

    public static Date getRefreshTokenExpiration(String memberId) {
        return new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION);
    }

    public static boolean isTokenExpired(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt == null || jwt.getExpiresAt().before(new Date());
    }
}