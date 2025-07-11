package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.domain.enums.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LoanTest {

    private Member member;
    private Book book;
    private LoanStatus loanStatus;
    private Staff staff;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setSex(Sex.MALE);

        book = new Book();
        loanStatus = new LoanStatus(LoanStatuses.LOANED);

        staff = new Staff();
        staff.setSex(Sex.FEMALE);
    }


    @Test
    void testConstructor_shouldInitializeFieldsCorrectly() {
        // Act
        Loan loan = new Loan(member, book, loanStatus, staff);

        // Assert
        assertThat(loan.getMember()).isEqualTo(member);
        assertThat(loan.getBook()).isEqualTo(book);
        assertThat(loan.getStatus()).isEqualTo(loanStatus);
        assertThat(loan.getStaff()).isEqualTo(staff);
        assertThat(loan.getLoanDate()).isNotNull();
        assertThat(loan.getDueDate()).isEqualTo(loan.getLoanDate().plusDays(3));
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Loan loan = new Loan();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime returnDate = now.plusDays(2);

        // Act
        loan.setId(10L);
        loan.setMember(member);
        loan.setBook(book);
        loan.setLoanDate(now);
        loan.setDueDate(now.plusDays(5));
        loan.setReturnDate(returnDate);
        loan.setStatus(loanStatus);
        loan.setStaff(staff);

        // Assert
        assertThat(loan.getId()).isEqualTo(10L);
        assertThat(loan.getMember()).isEqualTo(member);
        assertThat(loan.getBook()).isEqualTo(book);
        assertThat(loan.getLoanDate()).isEqualTo(now);
        assertThat(loan.getDueDate()).isEqualTo(now.plusDays(5));
        assertThat(loan.getReturnDate()).isEqualTo(returnDate);
        assertThat(loan.getStatus()).isEqualTo(loanStatus);
        assertThat(loan.getStaff()).isEqualTo(staff);
    }

    @Test
    void testToString_shouldContainAllFields() {
        // Arrange
        Loan loan = new Loan(member, book, loanStatus, staff);

        // Act
        String str = loan.toString();

        // Assert
        // Assert
        assertThat(str).contains(
                "Loan{",
                "member=",
                "book=",
                "loanDate=",
                "dueDate=",
                "status=",
                "staff="
        );
    }
}
