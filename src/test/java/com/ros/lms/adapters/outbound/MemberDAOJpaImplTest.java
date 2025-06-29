package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.MemberDAOJpaImpl;
import com.ros.lms.domain.entities.Member;
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
        Member member = new Member("GOV123", "John", "", "Doe", "123456789", LocalDate.of(2000, 9, 15), 'M', "john@example.com", "johndoe");
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
        Member member = new Member("GOV456", "Jane", "", "Smith", "987654321", LocalDate.of(2001, 12, 25), 'F', "jane@example.com", "janesmith");
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
        Member member = new Member("GOV789", "Peter", "", "Zeus", "987654999", LocalDate.of(1966, 4, 23), 'F', "peter@example.com", "peterzeus");
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
