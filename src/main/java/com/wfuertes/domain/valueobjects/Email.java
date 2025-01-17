package com.wfuertes.domain.valueobjects;

import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    private static final int MAX_LOCAL_PART_LENGTH = 64;
    private static final int MAX_DOMAIN_PART_LENGTH = 255;

    public Email {
        validateEmail(value);
    }

    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        email = email.trim();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }

        String[] parts = email.split("@");
        String localPart = parts[0];
        String domainPart = parts[1];

        if (localPart.length() > MAX_LOCAL_PART_LENGTH) {
            throw new IllegalArgumentException("Local part exceeds maximum length of %d: %s".formatted(MAX_LOCAL_PART_LENGTH, email));
        }

        if (domainPart.length() > MAX_DOMAIN_PART_LENGTH) {
            throw new IllegalArgumentException("Domain part exceeds maximum length of %d: %s".formatted(MAX_DOMAIN_PART_LENGTH, email));
        }

        validateDomainPart(domainPart, email);
    }

    private static void validateDomainPart(String domain, String fullEmail) {
        if (domain.contains("..")) {
            throw new IllegalArgumentException("Domain cannot contain consecutive dots: " + fullEmail);
        }

        if (domain.startsWith(".") || domain.endsWith(".")) {
            throw new IllegalArgumentException("Domain cannot start or end with a dot: " + fullEmail);
        }

        String[] domainParts = domain.split("\\.");
        if (domainParts.length < 2) {
            throw new IllegalArgumentException("Domain must have at least two parts: " + fullEmail);
        }

        for (String part : domainParts) {
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Domain parts cannot be empty: " + fullEmail);
            }
            if (part.length() > 63) {
                throw new IllegalArgumentException("Domain part exceeds maximum length of 63: " + fullEmail);
            }
        }
    }

    public static Email of(String email) {
        return new Email(email);
    }
}
