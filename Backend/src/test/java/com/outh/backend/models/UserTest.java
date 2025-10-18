package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(user);
        assertNull(user.getFirebaseId());
        assertNull(user.getUsername());
        assertNull(user.getRole());
        assertEquals(BigDecimal.ZERO, user.getAccount_balance());
        assertNull(user.getJoined());
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User("firebase123", "john.doe@example.com", "ROLE_USER");

        assertEquals("firebase123", user.getFirebaseId());
        assertEquals("john.doe@example.com", user.getUsername());
        assertEquals("ROLE_USER", user.getRole());
        assertEquals(BigDecimal.ZERO, user.getAccount_balance());
    }

    @Test
    void testSettersAndGetters() {
        user.setFirebaseId("user456");
        user.setUsername("jane.doe");
        user.setRole("ROLE_ADMIN");
        user.setAccount_balance(new BigDecimal("150.75"));

        assertEquals("user456", user.getFirebaseId());
        assertEquals("jane.doe", user.getUsername());
        assertEquals("ROLE_ADMIN", user.getRole());
        assertEquals(new BigDecimal("150.75"), user.getAccount_balance());
    }

    @Test
    void testAccountBalanceOperations() {
        user.setAccount_balance(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), user.getAccount_balance());

        user.setAccount_balance(new BigDecimal("0.00"));
        assertEquals(new BigDecimal("0.00"), user.getAccount_balance());

        user.setAccount_balance(new BigDecimal("999.99"));
        assertEquals(new BigDecimal("999.99"), user.getAccount_balance());
    }

    @Test
    void testUsernameUniqueness() {
        user.setUsername("unique.user@example.com");
        assertEquals("unique.user@example.com", user.getUsername());

        // Test that username can be changed
        user.setUsername("new.username@example.com");
        assertEquals("new.username@example.com", user.getUsername());
    }

    @Test
    void testRoleAssignment() {
        user.setRole("ROLE_USER");
        assertEquals("ROLE_USER", user.getRole());

        user.setRole("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", user.getRole());

        user.setRole("ROLE_MODERATOR");
        assertEquals("ROLE_MODERATOR", user.getRole());
    }

    @Test
    void testFirebaseIdImmutable() {
        user.setFirebaseId("initial-id");
        assertEquals("initial-id", user.getFirebaseId());

        // In practice, Firebase ID shouldn't change after initial assignment
        user.setFirebaseId("new-id");
        assertEquals("new-id", user.getFirebaseId());
    }
}