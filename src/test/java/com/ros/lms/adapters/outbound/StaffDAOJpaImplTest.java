package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.StaffDAOJpaImpl;
import com.ros.lms.domain.entities.Staff;
import com.ros.lms.domain.entities.User;
import com.ros.lms.domain.enums.Sex;
import com.ros.lms.ports.outbound.repository_contracts.StaffDAO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // This activates application-test.properties
class StaffDAOJpaImplTest {
    @Autowired
    private EntityManager entityManager;

    private StaffDAO staffDAO;

    @BeforeEach
    void setUp() {
        staffDAO = new StaffDAOJpaImpl(entityManager);
    }

    @Test
    @Transactional
    void testFindByUsername() {
        // Create and persist User first
        User user = new User();
        user.setUsername("janesmith");
        user.setEmail("jane@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        // Create Staff linked to User
        Staff staff = new Staff.Builder()
                .governmentID("GOV456")
                .firstName("Jane")
                .lastName("Smith")
                .phone("987654321")
                .sex(Sex.FEMALE)
                .user(user)
                .build();

        entityManager.persist(staff);

        // Act
        Optional<Staff> found = staffDAO.findByUsername("janesmith");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Jane");
        assertThat(found.get().getUser().getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    @Transactional
    void findByUsername_withEmptyString_shouldReturnEmptyOptional() {
        Optional<Staff> found = staffDAO.findByUsername("");

        assertThat(found).isEmpty();
    }

}
