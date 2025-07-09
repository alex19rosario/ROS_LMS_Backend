package com.ros.lms.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorityIdTest {

    private AuthorityId authorityId;
    private final String testUsername = "johndoe";
    private final String testAuthority = "ROLE_ADMIN";

    @BeforeEach
    void setUp() {
        authorityId = new AuthorityId(testUsername, testAuthority);
    }

    @Test
    void testGettersAndSetters() {
        // Test initial values from constructor
        assertEquals(testUsername, authorityId.getUsername());
        assertEquals(testAuthority, authorityId.getAuthority());

        // Test setters
        String newUsername = "janedoe";
        authorityId.setUsername(newUsername);
        assertEquals(newUsername, authorityId.getUsername());

        String newAuthority = "ROLE_USER";
        authorityId.setAuthority(newAuthority);
        assertEquals(newAuthority, authorityId.getAuthority());
    }

    @Test
    void testConstructors() {
        // Test parameterized constructor
        AuthorityId paramAuthId = new AuthorityId(testUsername, testAuthority);
        assertEquals(testUsername, paramAuthId.getUsername());
        assertEquals(testAuthority, paramAuthId.getAuthority());

        // Test no-arg constructor
        AuthorityId emptyAuthId = new AuthorityId();
        assertNull(emptyAuthId.getUsername());
        assertNull(emptyAuthId.getAuthority());
    }

    @Test
    void testEquals_SameInstance() {
        assertEquals(authorityId, authorityId);
    }

    @Test
    void testEquals_EqualObjects() {
        AuthorityId sameAuthId = new AuthorityId(testUsername, testAuthority);
        assertEquals(authorityId, sameAuthId);
        assertEquals(authorityId.hashCode(), sameAuthId.hashCode());
    }

    @Test
    void testEquals_DifferentUsername() {
        AuthorityId differentAuthId = new AuthorityId("differentUser", testAuthority);
        assertNotEquals(authorityId, differentAuthId);
        assertNotEquals(authorityId.hashCode(), differentAuthId.hashCode());
    }

    @Test
    void testEquals_DifferentAuthority() {
        AuthorityId differentAuthId = new AuthorityId(testUsername, "ROLE_USER");
        assertNotEquals(authorityId, differentAuthId);
        assertNotEquals(authorityId.hashCode(), differentAuthId.hashCode());
    }

    @Test
    void testEquals_NullComparison() {
        assertNotEquals(null, authorityId);
    }

    @Test
    void testEquals_DifferentClass() {
        assertNotEquals("Not an AuthorityId object", authorityId);
    }

    @Test
    void testNullValues() {
        authorityId.setUsername(null);
        authorityId.setAuthority(null);

        assertNull(authorityId.getUsername());
        assertNull(authorityId.getAuthority());

        // Test equals with null values
        AuthorityId nullAuthId = new AuthorityId(null, null);
        assertEquals(new AuthorityId(null, null), nullAuthId);
        assertNotEquals(new AuthorityId("user", null), nullAuthId);
        assertNotEquals(new AuthorityId(null, "ROLE"), nullAuthId);
    }

    @Test
    void testEmptyStrings() {
        authorityId.setUsername("");
        authorityId.setAuthority(" ");

        assertEquals("", authorityId.getUsername());
        assertEquals(" ", authorityId.getAuthority());

        // Test equals with empty strings
        AuthorityId emptyAuthId = new AuthorityId("", " ");
        assertEquals(new AuthorityId("", " "), emptyAuthId);
        assertNotEquals(new AuthorityId(" ", " "), emptyAuthId);
        assertNotEquals(new AuthorityId("", ""), emptyAuthId);
    }
}
