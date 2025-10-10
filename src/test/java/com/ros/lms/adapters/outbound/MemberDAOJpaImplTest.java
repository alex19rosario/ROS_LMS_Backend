package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.MemberDAOJpaImpl;
import com.ros.lms.domain.entities.Member;
import com.ros.lms.domain.entities.User;
import com.ros.lms.domain.enums.MemberValidationStatus;
import com.ros.lms.domain.enums.Sex;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

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

        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        Member member = new Member.Builder()
                .governmentID("GOV123")
                .firstName("John")
                .lastName("Doe")
                .phone("9876543211")
                .dateOfBirth(LocalDate.of(2000, 9, 15))
                .sex(Sex.MALE)
                .user(user)
                .build();

        memberDAO.create(member);

        Optional<Member> found = memberDAO.findByGovernmentID("GOV123");

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getEmail()).isEqualTo("john@example.com");
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
        User user = new User();
        user.setUsername("janesmith");
        user.setEmail("jane@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        Member member = new Member.Builder()
                .governmentID("GOV456")
                .firstName("Jane")
                .lastName("Smith")
                .phone("987654321")
                .dateOfBirth(LocalDate.of(2001, 12, 25))
                .sex(Sex.FEMALE)
                .user(user)
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
        User user = new User();
        user.setUsername("peterzeus");
        user.setEmail("peter@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        Member member = new Member.Builder()
                .governmentID("GOV789")
                .firstName("Peter")
                .lastName("Zeus")
                .phone("987654999")
                .dateOfBirth(LocalDate.of(1966, 4, 23))
                .sex(Sex.MALE) // fixed from FEMALE (typo in your snippet)
                .user(user)
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

    @Test
    @Transactional
    void validateMemberUniqueness_whenNoConflicts_shouldReturnEmptySet() {
        // given
        User user = new User();
        user.setUsername("uniqueuser");
        user.setEmail("unique@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        Member member = new Member.Builder()
                .governmentID("UNIQUE_GOV")
                .firstName("Unique")
                .lastName("User")
                .phone("9876543210")
                .dateOfBirth(LocalDate.of(1995, 5, 5))
                .sex(Sex.MALE)
                .user(user)
                .build();

        entityManager.persist(member);
        entityManager.flush();

        // when
        Set<MemberValidationStatus> result =
                memberDAO.validateMemberUniqueness("DIFFERENT_GOV", "differentuser", "different@example.com");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @Transactional
    void validateMemberUniqueness_whenGovernmentIDExists_shouldReturnGovernmentIdConflict() {
        // given
        User user = new User();
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        Member member = new Member.Builder()
                .governmentID("GOV001")
                .firstName("User")
                .lastName("One")
                .phone("123456789")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .sex(Sex.MALE)
                .user(user)
                .build();

        entityManager.persist(member);
        entityManager.flush();

        // when
        Set<MemberValidationStatus> result =
                memberDAO.validateMemberUniqueness("GOV001", "newuser", "new@example.com");

        // then
        assertThat(result).containsExactly(MemberValidationStatus.GOVERNMENT_ID_EXISTS);
    }

    @Test
    @Transactional
    void validateMemberUniqueness_whenUsernameExists_shouldReturnUsernameConflict() {
        // given
        User user = new User();
        user.setUsername("conflictuser");
        user.setEmail("conflict@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        Member member = new Member.Builder()
                .governmentID("GOV002")
                .firstName("Conflict")
                .lastName("User")
                .phone("987654321")
                .dateOfBirth(LocalDate.of(1990, 2, 2))
                .sex(Sex.MALE)
                .user(user)
                .build();

        entityManager.persist(member);
        entityManager.flush();

        // when
        Set<MemberValidationStatus> result =
                memberDAO.validateMemberUniqueness("DIFFERENT_GOV", "conflictuser", "another@example.com");

        // then
        assertThat(result).containsExactly(MemberValidationStatus.USERNAME_EXISTS);
    }

    @Test
    @Transactional
    void validateMemberUniqueness_whenEmailExists_shouldReturnEmailConflict() {
        // given
        User user = new User();
        user.setUsername("emailuser");
        user.setEmail("email@exists.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        Member member = new Member.Builder()
                .governmentID("GOV003")
                .firstName("Email")
                .lastName("User")
                .phone("111222333")
                .dateOfBirth(LocalDate.of(1992, 3, 3))
                .sex(Sex.MALE)
                .user(user)
                .build();

        entityManager.persist(member);
        entityManager.flush();

        // when
        Set<MemberValidationStatus> result =
                memberDAO.validateMemberUniqueness("DIFFERENT_GOV", "differentuser", "email@exists.com");

        // then
        assertThat(result).containsExactly(MemberValidationStatus.EMAIL_EXISTS);
    }

    @Test
    @Transactional
    void validateMemberUniqueness_whenMultipleConflicts_shouldReturnAllRelevantStatuses() {
        // given
        User user = new User();
        user.setUsername("multiuser");
        user.setEmail("multi@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        Member member = new Member.Builder()
                .governmentID("GOV_MULTI")
                .firstName("Multi")
                .lastName("Conflict")
                .phone("444555666")
                .dateOfBirth(LocalDate.of(1993, 4, 4))
                .sex(Sex.MALE)
                .user(user)
                .build();

        entityManager.persist(member);
        entityManager.flush();

        // when
        Set<MemberValidationStatus> result =
                memberDAO.validateMemberUniqueness("GOV_MULTI", "multiuser", "multi@example.com");

        // then
        assertThat(result)
                .containsExactlyInAnyOrder(
                        MemberValidationStatus.GOVERNMENT_ID_EXISTS,
                        MemberValidationStatus.USERNAME_EXISTS,
                        MemberValidationStatus.EMAIL_EXISTS
                );
    }
}
