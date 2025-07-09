package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.MemberDAOJpaImpl;
import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.Loan;
import com.ros.lms.domain.entities.LoanStatus;
import com.ros.lms.domain.entities.Member;
import com.ros.lms.domain.enums.MemberStatuses;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")  // ensures it picks application-test.yml
class MemberDAOJpaImplTest {

    @Autowired
    private EntityManager entityManager;

    private MemberDAOJpaImpl memberDAO;

    @BeforeEach
    void setUp() {
        memberDAO = new MemberDAOJpaImpl(entityManager);
    }

    @Test
    @Transactional
    void testCreateAndFindByGovernmentID() {
        Member member = new Member.Builder()
                .governmentID("GOV123")
                .firstName("John")
                .lastName("Doe")
                .phone("9876543211")
                .dateOfBirth(LocalDate.of(2000, 9, 15))
                .sex('M')
                .email("john@example.com")
                .username("johndoe")
                .build();

        memberDAO.create(member);

        Optional<Member> found = memberDAO.findByGovernmentID("GOV123");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @Transactional
    void findByGovernmentID_whenMemberNotExists_shouldReturnEmptyOptional() {
        // No setup needed - we're testing for non-existent member
        Optional<Member> found = memberDAO.findByGovernmentID("NON_EXISTENT_ID");

        assertThat(found).isEmpty();
    }

    @Test
    @Transactional
    void testFindByUsername() {
        Member member = new Member.Builder()
                .governmentID("GOV456")
                .firstName("Jane")
                .lastName("Smith")
                .phone("987654321")
                .dateOfBirth(LocalDate.of(2001, 12, 25))
                .sex('F')
                .email("jane@example.com")
                .username("janesmith")
                .build();

        memberDAO.create(member);

        Optional<Member> found = memberDAO.findByUsername("janesmith");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Jane");
    }

    @Test
    @Transactional
    void findByUsername_withEmptyString_shouldReturnEmptyOptional() {
        Optional<Member> found = memberDAO.findByUsername("");

        assertThat(found).isEmpty();
    }

    @Test
    @Transactional
    void testFindByEmail() {
        Member member = new Member.Builder()
                .governmentID("GOV789")
                .firstName("Peter")
                .lastName("Zeus")
                .phone("987654999")
                .dateOfBirth(LocalDate.of(1966, 4, 23))
                .sex('F')
                .email("peter@example.com")
                .username("peterzeus")
                .build();
        memberDAO.create(member);

        Optional<Member> found = memberDAO.findByEmail("peter@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Peter");
    }

    @Test
    @Transactional
    void testFindByEmailNotFound() {
        Optional<Member> found = memberDAO.findByEmail("not@found.com");
        assertThat(found).isNotPresent();
    }
}
