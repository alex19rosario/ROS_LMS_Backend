package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.AuthorityTypeDAOJpaImpl;
import com.ros.lms.domain.entities.AuthorityType;
import com.ros.lms.domain.enums.RoleType;
import com.ros.lms.ports.outbound.repository_contracts.AuthorityTypeDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthorityTypeDAOJpaImplTest {

    @Autowired
    private EntityManager entityManager;

    private AuthorityTypeDAO authorityTypeDAO;

    @BeforeEach
    void setUp() {
        authorityTypeDAO = new AuthorityTypeDAOJpaImpl(entityManager);
    }

    @Transactional
    @Test
    void testFindByLabel_AuthorityTypeExists() {
        // Arrange
        AuthorityType authorityType = new AuthorityType(RoleType.MEMBER);
        entityManager.persist(authorityType);
        entityManager.flush();
        entityManager.clear(); // ensure DB read, not cache

        // Act
        Optional<AuthorityType> result = authorityTypeDAO.findByLabel(RoleType.MEMBER);

        // Assert
        assertTrue(result.isPresent(), "AuthorityType should be found in the database.");
        assertEquals(RoleType.MEMBER, result.get().getLabel());
    }

    @Transactional
    @Test
    void testFindByLabel_AuthorityTypeDoesNotExist() {
        // Act
        Optional<AuthorityType> result = authorityTypeDAO.findByLabel(RoleType.STAFF);

        // Assert
        assertFalse(result.isPresent(), "AuthorityType should not be found in the database.");
    }

}
