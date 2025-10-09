package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.LoanDAOJpaImpl;
import com.ros.lms.domain.entities.*;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.domain.enums.Sex;
import com.ros.lms.ports.outbound.repository_contracts.LoanDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test") // This activates application-test.properties
class LoanDAOJpaImplTest {
    @Autowired
    private EntityManager entityManager;

    private LoanDAO loanDAO;

    @BeforeEach
    void setUp() {
        loanDAO = new LoanDAOJpaImpl(entityManager);
    }

    @Test
    @Transactional
    void create_shouldPersistLoan() {

        // Create User for Member
        User memberUser = new User();
        memberUser.setUsername("johndoe");
        memberUser.setEmail("john.doe@example.com");
        memberUser.setPassword("secret"); // in real case -> encoded
        memberUser.setEnabled(true);
        entityManager.persist(memberUser);


        // Create Member
        Member member = new Member();
        member.setGovernmentID("GOV123");
        member.setFirstName("John");
        member.setMiddleName("A");
        member.setLastName("Doe");
        member.setPhone("1234567890");
        member.setDateOfBirth(LocalDate.of(1990, 1, 1));
        member.setSex(Sex.MALE);
        member.setUser(memberUser);
        entityManager.persist(member);

        Book book = new Book();
        book.setIsbn("1234567890123");
        book.setTitle("Effective Java");
        book.setAvailable(true);
        book.setCoverImagePath("cover.jpg");
        entityManager.persist(book);

        LoanStatus status = new LoanStatus(LoanStatuses.LOANED);
        entityManager.persist(status);

        // Create User for Staff
        User staffUser = new User();
        staffUser.setUsername("janesmith");
        staffUser.setEmail("jane.smith@example.com");
        staffUser.setPassword("secret");
        staffUser.setEnabled(true);
        entityManager.persist(staffUser);

        // Create Staff
        Staff staff = new Staff();
        staff.setGovernmentID("STAFF123");
        staff.setFirstName("Jane");
        staff.setMiddleName("B");
        staff.setLastName("Smith");
        staff.setPhone("9876543210");
        staff.setSex(Sex.FEMALE);
        staff.setUser(staffUser);
        entityManager.persist(staff);

        // Flush to ensure entities get IDs
        entityManager.flush();

        // Act: create and persist the Loan
        Loan loan = new Loan(member, book, status, staff);
        loanDAO.create(loan);
        entityManager.flush(); // Force SQL insert to happen

        // Assert: retrieve and verify
        Loan persistedLoan = entityManager.find(Loan.class, loan.getId());

        assertThat(persistedLoan).isNotNull();
        assertThat(persistedLoan.getBook().getTitle()).isEqualTo("Effective Java");
        assertThat(persistedLoan.getMember().getFirstName()).isEqualTo("John");
        assertThat(persistedLoan.getStatus().getCode()).isEqualTo(LoanStatuses.LOANED);
        assertThat(persistedLoan.getStaff().getUser().getUsername()).isEqualTo("janesmith");
    }

    @Test
    @Transactional
    void findActiveLoanByBook_shouldReturnActiveLoan_whenExists() {
        // Arrange
        // Reuse helper method to create test data (same pattern as your create test)
        User memberUser = new User();
        memberUser.setUsername("activeUser");
        memberUser.setEmail("active@example.com");
        memberUser.setPassword("pass123");
        memberUser.setEnabled(true);
        entityManager.persist(memberUser);

        Member member = new Member();
        member.setGovernmentID("GOV999");
        member.setFirstName("Active");
        member.setMiddleName("A");
        member.setLastName("User");
        member.setPhone("123123123");
        member.setDateOfBirth(LocalDate.of(1995, 5, 15));
        member.setSex(Sex.MALE);
        member.setUser(memberUser);
        entityManager.persist(member);

        Book book = new Book();
        book.setIsbn("9999999999999");
        book.setTitle("Clean Code");
        book.setAvailable(false);
        book.setCoverImagePath("cover2.jpg");
        entityManager.persist(book);

        LoanStatus activeStatus = new LoanStatus(LoanStatuses.LOANED);
        entityManager.persist(activeStatus);

        User staffUser = new User();
        staffUser.setUsername("staffUser");
        staffUser.setEmail("staff@example.com");
        staffUser.setPassword("secret");
        staffUser.setEnabled(true);
        entityManager.persist(staffUser);

        Staff staff = new Staff();
        staff.setGovernmentID("STAFF999");
        staff.setFirstName("Jane");
        staff.setLastName("Doe");
        staff.setPhone("5555555555");
        staff.setSex(Sex.FEMALE);
        staff.setUser(staffUser);
        entityManager.persist(staff);

        entityManager.flush();

        // Create loan with active status
        Loan loan = new Loan(member, book, activeStatus, staff);
        entityManager.persist(loan);
        entityManager.flush();

        // Act
        var result = loanDAO.findActiveLoanByBook(book);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getBook().getTitle()).isEqualTo("Clean Code");
        assertThat(result.get().getStatus().getCode()).isEqualTo(LoanStatuses.LOANED);
    }

    @Test
    @Transactional
    void findActiveLoanByBook_shouldReturnEmpty_whenNoActiveLoanExists() {
        // Arrange
        Book book = new Book();
        book.setIsbn("1111111111111");
        book.setTitle("Non Active Book");
        entityManager.persist(book);

        LoanStatus returnedStatus = new LoanStatus(LoanStatuses.RETURNED);
        entityManager.persist(returnedStatus);

        User memberUser = new User();
        memberUser.setUsername("nonactive");
        memberUser.setEmail("nonactive@example.com");
        memberUser.setPassword("pass123");
        memberUser.setEnabled(true);
        entityManager.persist(memberUser);

        Member member = new Member();
        member.setGovernmentID("GOV000");
        member.setFirstName("Non");
        member.setLastName("Active");
        member.setPhone("0000000000");
        member.setUser(memberUser);
        member.setSex(Sex.MALE);
        entityManager.persist(member);

        User staffUser = new User();
        staffUser.setUsername("staffInactive");
        staffUser.setEmail("inactive@example.com");
        staffUser.setPassword("secret");
        staffUser.setEnabled(true);
        entityManager.persist(staffUser);

        Staff staff = new Staff();
        staff.setGovernmentID("STAFF000");
        staff.setFirstName("In");
        staff.setLastName("Active");
        staff.setPhone("9999999999");
        staff.setSex(Sex.FEMALE);
        staff.setUser(staffUser);
        entityManager.persist(staff);

        Loan loan = new Loan(member, book, returnedStatus, staff);
        entityManager.persist(loan);
        entityManager.flush();

        // Act
        var result = loanDAO.findActiveLoanByBook(book);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @Transactional
    void update_shouldMergeLoan() {
        // Arrange
        User memberUser = new User();
        memberUser.setUsername("updateUser");
        memberUser.setEmail("update@example.com");
        memberUser.setPassword("pass123");
        memberUser.setEnabled(true);
        entityManager.persist(memberUser);

        Member member = new Member();
        member.setGovernmentID("GOVUPDATE");
        member.setFirstName("Update");
        member.setLastName("User");
        member.setPhone("222333444");
        member.setUser(memberUser);
        member.setSex(Sex.MALE);
        entityManager.persist(member);

        Book book = new Book();
        book.setIsbn("2222222222222");
        book.setTitle("Domain-Driven Design");
        entityManager.persist(book);

        LoanStatus status = new LoanStatus(LoanStatuses.LOANED);
        entityManager.persist(status);

        User staffUser = new User();
        staffUser.setUsername("staffUpdate");
        staffUser.setEmail("staffupdate@example.com");
        staffUser.setPassword("secret");
        staffUser.setEnabled(true);
        entityManager.persist(staffUser);

        Staff staff = new Staff();
        staff.setGovernmentID("STAFFUPDATE");
        staff.setFirstName("Staff");
        staff.setLastName("Update");
        staff.setPhone("9999998888");
        staff.setSex(Sex.FEMALE);
        staff.setUser(staffUser);
        entityManager.persist(staff);

        entityManager.flush();

        Loan loan = new Loan(member, book, status, staff);
        entityManager.persist(loan);
        entityManager.flush();

        // Act: update the status to RETURNED
        LoanStatus newStatus = new LoanStatus(LoanStatuses.RETURNED);
        entityManager.persist(newStatus);
        loan.setStatus(newStatus);

        loanDAO.update(loan);
        entityManager.flush();

        // Assert
        Loan updatedLoan = entityManager.find(Loan.class, loan.getId());
        assertThat(updatedLoan).isNotNull();
        assertThat(updatedLoan.getStatus().getCode()).isEqualTo(LoanStatuses.RETURNED);
    }

}
