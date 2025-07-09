package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.LoanDAOJpaImpl;
import com.ros.lms.domain.entities.*;
import com.ros.lms.ports.outbound.repository_contracts.LoanDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@SpringBootTest
@ActiveProfiles("test") // This activates application-test.properties
public class LoanDAOJpaImplTest {
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
        // Arrange: create and persist dependent entities
        Member member = new Member();
        member.setGovernmentID("GOV123");
        member.setFirstName("John");
        member.setMiddleName("A");
        member.setLastName("Doe");
        member.setPhone("1234567890");
        member.setDateOfBirth(LocalDate.of(1990, 1, 1));
        member.setSex('M');
        member.setEmail("john.doe@example.com");
        member.setUsername("johndoe");
        entityManager.persist(member);

        Book book = new Book();
        book.setIsbn(1234567890123L);
        book.setTitle("Effective Java");
        book.setAvailable(true);
        book.setCoverImagePath("cover.jpg");
        entityManager.persist(book);

        LoanStatus status = new LoanStatus();
        status.setDescription("ONGOING");
        entityManager.persist(status);

        Staff staff = new Staff();
        staff.setGovernmentID("STAFF123");
        staff.setFirstName("Jane");
        staff.setMiddleName("B");
        staff.setLastName("Smith");
        staff.setPhone("9876543210");
        staff.setSex('F');
        staff.setEmail("jane.smith@example.com");
        staff.setUsername("janesmith");
        entityManager.persist(staff);

        // Flush to ensure entities get IDs
        entityManager.flush();

        // Act: create and persist the Loan
        Loan loan = new Loan(member, book, status, staff);
        loanDAO.create(loan);
        entityManager.flush(); // Force SQL insert to happen

        // Assert: retrieve and verify
        Loan persistedLoan = entityManager.find(Loan.class, loan.getId());
        assert persistedLoan != null;
        assert persistedLoan.getBook().getTitle().equals("Effective Java");
        assert persistedLoan.getMember().getFirstName().equals("John");
        assert persistedLoan.getStatus().getDescription().equals("ONGOING");
        assert persistedLoan.getStaff().getUsername().equals("janesmith");
    }

}
