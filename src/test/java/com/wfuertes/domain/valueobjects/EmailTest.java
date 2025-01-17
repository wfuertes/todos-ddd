package com.wfuertes.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    void testValidEmail() {
        assertDoesNotThrow(() -> new Email("test@example.com"));
    }

    @Test
    void testInvalidEmailFormat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Email("invalid-email"));
        assertEquals("Invalid email format: invalid-email", exception.getMessage());
    }

    @Test
    void testNullEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Email(null));
        assertEquals("Email cannot be null or empty", exception.getMessage());
    }

    @Test
    void testEmptyEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Email(""));
        assertEquals("Email cannot be null or empty", exception.getMessage());
    }

    @Test
    void testLocalPartTooLong() {
        String localPart = "a".repeat(65);
        String email = localPart + "@example.com";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Email(email));
        assertEquals("Local part exceeds maximum length of 64: " + email, exception.getMessage());
    }

    @Test
    void testDomainPartTooLong() {
        String domainPart = "a".repeat(256);
        String email = "test@" + domainPart;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Email(email));
        assertEquals("Domain part exceeds maximum length of 255: " + email, exception.getMessage());
    }

    @Test
    void testDomainWithConsecutiveDots() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Email("test@exa..mple.com"));
        assertEquals("Domain cannot contain consecutive dots: test@exa..mple.com", exception.getMessage());
    }

    @Test
    void testDomainStartsWithDot() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Email("test@.example.com"));
        assertEquals("Domain cannot start or end with a dot: test@.example.com", exception.getMessage());
    }

    @Test
    void testDomainEndsWithDot() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Email("test@example.com."));
        assertEquals("Domain cannot start or end with a dot: test@example.com.", exception.getMessage());
    }
}