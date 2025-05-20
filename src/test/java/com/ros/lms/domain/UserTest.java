package com.ros.lms.domain;

import com.ros.lms.domain.entities.Authority;
import com.ros.lms.domain.entities.AuthorityId;
import com.ros.lms.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private final String testUsername = "testuser";
    private final String testPassword = "securePassword123";
    private final char testEnabled = 'Y';

    @BeforeEach
    void setUp() {
        user = new User(testUsername, testPassword, testEnabled);
    }

    @Test
    void testGettersAndSetters() {
        // Test initial values from constructor
        assertEquals(testUsername, user.getUsername());
        assertEquals(testPassword, user.getPassword());
        assertEquals(testEnabled, user.getEnabled());
        assertNull(user.getAuthorities());

        // Test setters
        String newUsername = "newuser";
        user.setUsername(newUsername);
        assertEquals(newUsername, user.getUsername());

        String newPassword = "newPassword456";
        user.setPassword(newPassword);
        assertEquals(newPassword, user.getPassword());

        char newEnabled = 'N';
        user.setEnabled(newEnabled);
        assertEquals(newEnabled, user.getEnabled());

        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(new AuthorityId(testUsername, "ROLE_USER")));
        user.setAuthorities(authorities);

        assertNotNull(user.getAuthorities());
        assertEquals(1, user.getAuthorities().size());
    }

    @Test
    void testConstructors() {
        // Test parameterized constructor
        User paramUser = new User(testUsername, testPassword, testEnabled);
        assertEquals(testUsername, paramUser.getUsername());
        assertEquals(testPassword, paramUser.getPassword());
        assertEquals(testEnabled, paramUser.getEnabled());
        assertNull(paramUser.getAuthorities());

        // Test no-arg constructor
        User emptyUser = new User();
        assertNull(emptyUser.getUsername());
        assertNull(emptyUser.getPassword());
        assertEquals('\u0000', emptyUser.getEnabled()); // char default value
        assertNull(emptyUser.getAuthorities());
    }

    @Test
    void testAuthoritiesManagement() {
        // Initially should be null
        assertNull(user.getAuthorities());

        // Test setting authorities
        Set<Authority> authorities = new HashSet<>();
        Authority authority1 = new Authority(new AuthorityId(testUsername, "ROLE_USER"));
        Authority authority2 = new Authority(new AuthorityId(testUsername, "ROLE_ADMIN"));
        authorities.add(authority1);
        authorities.add(authority2);

        user.setAuthorities(authorities);
        assertNotNull(user.getAuthorities());
        assertEquals(2, user.getAuthorities().size());

        // Test modifying the authorities set
        user.getAuthorities().remove(authority1);
        assertEquals(1, user.getAuthorities().size());
    }

    @Test
    void testNullValues() {
        user.setUsername(null);
        assertNull(user.getUsername());

        user.setPassword(null);
        assertNull(user.getPassword());

        user.setAuthorities(null);
        assertNull(user.getAuthorities());

        // Test constructor with null values
        User nullUser = new User(null, null, 'N');
        assertNull(nullUser.getUsername());
        assertNull(nullUser.getPassword());
        assertEquals('N', nullUser.getEnabled());
        assertNull(nullUser.getAuthorities());
    }

    @Test
    void testEmptyStrings() {
        user.setUsername("");
        assertEquals("", user.getUsername());

        user.setPassword(" ");
        assertEquals(" ", user.getPassword());

        // Test with empty authorities set
        user.setAuthorities(new HashSet<>());
        assertNotNull(user.getAuthorities());
        assertTrue(user.getAuthorities().isEmpty());
    }

    @Test
    void testToString() {
        // Test basic toString
        String toStringResult = user.toString();
        assertTrue(toStringResult.contains("username='" + testUsername + "'"));
        assertTrue(toStringResult.contains("password='" + testPassword + "'"));
        assertTrue(toStringResult.contains("enabled=" + testEnabled));
        assertTrue(toStringResult.contains("authorities=null"));

        // Test with authorities
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(new AuthorityId(testUsername, "ROLE_USER")));
        user.setAuthorities(authorities);

        toStringResult = user.toString();
        assertTrue(toStringResult.contains("authorities=" + authorities.toString()));

        // Test with null fields
        user.setUsername(null);
        user.setPassword(null);
        user.setAuthorities(null);
        toStringResult = user.toString();
        assertTrue(toStringResult.contains("username='null'"));
        assertTrue(toStringResult.contains("password='null'"));
        assertTrue(toStringResult.contains("authorities=null"));
    }

    @Test
    void testEnabledStatus() {
        // Test valid enabled values
        user.setEnabled('Y');
        assertEquals('Y', user.getEnabled());

        user.setEnabled('N');
        assertEquals('N', user.getEnabled());

        // Test invalid enabled values (should still work as it's just a char)
        user.setEnabled('X');
        assertEquals('X', user.getEnabled());

        user.setEnabled('1');
        assertEquals('1', user.getEnabled());
    }

}
