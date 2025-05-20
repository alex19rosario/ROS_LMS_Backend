package com.ros.lms.domain;

import com.ros.lms.domain.entities.Authority;
import com.ros.lms.domain.entities.AuthorityId;
import com.ros.lms.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorityTest {

    private Authority authority;
    private AuthorityId authorityId;
    private User user;

    private final String testUsername = "johndoe";
    private final String testAuthority = "ROLE_ADMIN";

    @BeforeEach
    void setUp() {
        authorityId = new AuthorityId(testUsername, testAuthority);
        user = mock(User.class); // Using Mockito for User dependency
        authority = new Authority(authorityId, user);
    }

    @Test
    void testGettersAndSetters() {
        // Test initial values from constructor
        assertEquals(authorityId, authority.getId());
        assertEquals(user, authority.getUser());
        assertEquals(testUsername, authority.getId().getUsername());
        assertEquals(testAuthority, authority.getId().getAuthority());

        // Test setters
        AuthorityId newId = new AuthorityId("newuser", "ROLE_USER");
        authority.setId(newId);
        assertEquals(newId, authority.getId());

        User newUser = mock(User.class);
        authority.setUser(newUser);
        assertEquals(newUser, authority.getUser());
    }

    @Test
    void testConstructors() {
        // Test minimal constructor
        Authority minimalAuthority = new Authority(authorityId);
        assertEquals(authorityId, minimalAuthority.getId());
        assertNull(minimalAuthority.getUser());

        // Test full constructor
        Authority fullAuthority = new Authority(authorityId, user);
        assertEquals(authorityId, fullAuthority.getId());
        assertEquals(user, fullAuthority.getUser());

        // Test no-arg constructor
        Authority emptyAuthority = new Authority();
        assertNull(emptyAuthority.getId());
        assertNull(emptyAuthority.getUser());
    }

    @Test
    void testNullValues() {
        // Test setting null ID
        authority.setId(null);
        assertNull(authority.getId());

        // Test setting null User
        authority.setUser(null);
        assertNull(authority.getUser());

        // Test constructor with null ID
        Authority nullIdAuthority = new Authority(null, user);
        assertNull(nullIdAuthority.getId());
        assertEquals(user, nullIdAuthority.getUser());

        // Test constructor with null User
        Authority nullUserAuthority = new Authority(authorityId, null);
        assertEquals(authorityId, nullUserAuthority.getId());
        assertNull(nullUserAuthority.getUser());
    }

    @Test
    void testToString() {
        String toStringResult = authority.toString();
        assertTrue(toStringResult.contains("username=" + testUsername));
        assertTrue(toStringResult.contains("role=" + testAuthority));
    }

    @Test
    void testUserAssociation() {
        // Verify user association
        when(user.getUsername()).thenReturn(testUsername);
        authority.setUser(user);

        assertEquals(user, authority.getUser());
        assertEquals(testUsername, authority.getUser().getUsername());

        // Verify the relationship
        verify(user, times(1)).getUsername();
    }

}
