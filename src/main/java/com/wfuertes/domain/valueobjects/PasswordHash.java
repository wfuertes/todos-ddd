package com.wfuertes.domain.valueobjects;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class PasswordHash {
    private final byte[] hash;
    private final byte[] salt;

    private PasswordHash(byte[] hash, byte[] salt) {
        if (hash == null || hash.length == 0) {
            throw new IllegalArgumentException("Hash value cannot be null or empty");
        }
        if (salt == null || salt.length == 0) {
            throw new IllegalArgumentException("Salt value cannot be null or empty");
        }

        this.hash = hash;
        this.salt = salt;
    }

    public static PasswordHash create(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password value cannot be null or empty");
        }

        try {
            final SecureRandom random = new SecureRandom();
            final byte[] salt = new byte[16];
            random.nextBytes(salt);

            final KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            final var hash = factory.generateSecret(spec).getEncoded();

            return new PasswordHash(hash, salt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    public static PasswordHash from(byte[] hash, byte[] salt) {
        return new PasswordHash(hash, salt);
    }

    public byte[] hash() {
        return hash;
    }

    public byte[] salt() {
        return salt;
    }

    public String toString() {
        return "PasswordHash{" +
                "hash=" + Arrays.toString(hash) +
                ", salt=" + Arrays.toString(salt) +
                '}';
    }
}
