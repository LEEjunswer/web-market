package com.webmarket.util.passwordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@Component
public class Sha256PasswordEncoderWithSalt implements PasswordEncoder {


    private static final int SALT_LENGTH = 16;
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            // Salt 생성
            byte[] salt = generateSalt();

            // Salt와 비밀번호 결합
            String saltedPassword = rawPassword.toString() + Base64.getEncoder().encodeToString(salt);

            // SHA-256 해시 적용
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(saltedPassword.getBytes());

            // Salt와 Hash 결합하여 반환
            return Base64.getEncoder().encodeToString(hash) + ":" + Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘 찾기 X", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 저장된 해시 값과 Salt 분리
        String[] parts = encodedPassword.split(":");
        String storedHash = parts[0];
        byte[] salt = Base64.getDecoder().decode(parts[1]);

        // 입력 받은 비밀번호와 Salt 결합 후 Hash 생성
        String saltedPassword = rawPassword.toString() + Base64.getEncoder().encodeToString(salt);
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(saltedPassword.getBytes());

            // 계산된 해시와 저장된 해시 비교
            return storedHash.equals(Base64.getEncoder().encodeToString(hash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘 찾기 X", e);
        }
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        // 업그레이드 처리 필요 없으므로 false 반환
        return false;
    }

    // Salt 생성
    private byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }
}