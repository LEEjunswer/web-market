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


    /* 유효기간 30일 앱 30일동안 안킬시 만료*/
    public static String generateToken(String memberId) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(memberId)
                .withIssuer("auth0")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                .sign(algorithm);
    }

    /*검증 하는 용도 */
    public static DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            return null;

        }
    }

    // 액세스 토큰 생성
    public static String generateAccessToken(String memberId) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(memberId)
                .withIssuer("auth0")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 15)) // 15분
                .sign(algorithm);
    }

    // 리프레시 토큰 생성
    public static String generateRefreshToken(String memberId) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Date expirationDate = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7);
        return JWT.create()
                .withSubject(memberId)
                .withIssuer("auth0")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7일
                .sign(algorithm);
    }

    /*3일 이하면 토큰을 재발급 용도*/
    public static Date getExpirationDate(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getExpiresAt();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
        public static String getMemberIdFromToken (String token){
            try {
                Algorithm algorithm = Algorithm.HMAC256(secretKey);
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT jwt = verifier.verify(token);

                return jwt.getSubject();
            } catch (JWTVerificationException e) {
                e.printStackTrace();
                return null;
            }
        }


    public static Date getRefreshTokenExpiration(String memberId) {
        return new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7);  // 7일
    }
    public static boolean isTokenExpired(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt == null || jwt.getExpiresAt().before(new Date());
        }

    }