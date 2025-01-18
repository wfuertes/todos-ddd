package com.wfuertes.domain.valueobjects;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Objects;

public class Password {
    private final byte[] hash;
    private final byte[] salt;

    private Password(byte[] hash, byte[] salt) {
        if (hash == null || hash.length == 0) {
            throw new IllegalArgumentException("Hash value cannot be null or empty");
        }
        if (salt == null || salt.length == 0) {
            throw new IllegalArgumentException("Salt value cannot be null or empty");
        }
        this.hash = hash;
        this.salt = salt;
    }

    public static Password create(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password value cannot be null or empty");
        }

        try {
            final SecureRandom random = new SecureRandom();
            final byte[] salt = new byte[16];
            random.nextBytes(salt);

            var hash = createHash(password, salt);
            return new Password(hash, salt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    public static Password from(byte[] hash, byte[] salt) {
        return new Password(hash, salt);
    }

    public byte[] hash() {
        return hash;
    }

    public byte[] salt() {
        return salt;
    }

    public boolean validate(String password) {
        var hash = createHash(password, salt);
        return Arrays.equals(this.hash, hash);
    }

    @Override
    public String toString() {
        return "Password{" +
                "hash=" + Arrays.toString(hash) +
                ", salt=" + Arrays.toString(salt) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password that = (Password) o;
        return Objects.deepEquals(hash, that.hash) && Objects.deepEquals(salt, that.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(hash), Arrays.hashCode(salt));
    }

    private static byte[] createHash(String password, byte[] salt) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password value cannot be null or empty");
        }
        if (salt == null || salt.length == 0) {
            throw new IllegalArgumentException("Salt value cannot be null or empty");
        }
        try {
            final KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }
}
