package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.UserDAOJpaImpl;
import com.ros.lms.domain.entities.AuthorityType;
import com.ros.lms.domain.entities.User;
import com.ros.lms.domain.enums.RoleType;
import com.ros.lms.ports.outbound.repository_contracts.UserDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // This activates application-test.properties
class UserDAOJpaImplTest {
    @Autowired
    private EntityManager entityManager;

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOJpaImpl(entityManager);
    }

    @Transactional
    @Test
    void testCreateUser() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testuser@example.com");
        user.setPassword("testPassword");
        user.setEnabled(true);

        Set<AuthorityType> authorities = new HashSet<>();
        AuthorityType authority = new AuthorityType(RoleType.MEMBER);
        entityManager.persist(authority); // persist it first!
        authorities.add(authority);
        user.setAuthorities(authorities);

        // Act
        userDAO.create(user);
        entityManager.flush();

        User persistedUser = entityManager
                .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", "testUser")
                .getSingleResult();

        // Assert
        assertNotNull(persistedUser, "User should be persisted in the database.");
        assertEquals("testUser", persistedUser.getUsername());
        assertEquals("testuser@example.com", persistedUser.getEmail());
        assertEquals("testPassword", persistedUser.getPassword());
        assertTrue(persistedUser.getEnabled());
        assertNotNull(persistedUser.getAuthorities());
        assertEquals(1, persistedUser.getAuthorities().size());
    }

    @Transactional
    @Test
    void testFindByUsername_UserExists() {
        // Arrange
        User user = new User();
        user.setUsername("existingUser");
        user.setEmail("existing@example.com");
        user.setPassword("password123");
        user.setEnabled(true);

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear(); // Clear the cache to ensure we read from DB

        // Act
        Optional<User> result = userDAO.findByUsername("existingUser");

        // Assert
        assertTrue(result.isPresent(), "User should be found in the database.");
        assertEquals("existingUser", result.get().getUsername());
        assertEquals("existing@example.com", result.get().getEmail());
        assertEquals("password123", result.get().getPassword());
        assertTrue(result.get().getEnabled());
    }

    @Transactional
    @Test
    void testFindByUsername_UserDoesNotExist() {
        // Act
        Optional<User> result = userDAO.findByUsername("nonExistentUser");

        // Assert
        assertFalse(result.isPresent(), "User should not be found in the database.");
    }
}
