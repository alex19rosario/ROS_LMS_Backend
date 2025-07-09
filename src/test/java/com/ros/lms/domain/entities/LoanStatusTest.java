package com.ros.lms.domain.entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoanStatusTest {

    @Test
    void testNoArgConstructor_shouldCreateEmptyLoanStatus() {
        LoanStatus loanStatus = new LoanStatus();

        assertThat(loanStatus).isNotNull();
        assertThat(loanStatus.getDescription()).isNull();
        assertThat(loanStatus.getLoans()).isNull(); // default value before set
    }

    @Test
    void testConstructorWithDescription_shouldSetDescription() {
        LoanStatus loanStatus = new LoanStatus("LOANED");

        assertThat(loanStatus.getDescription()).isEqualTo("LOANED");
    }

    @Test
    void testSettersAndGetters_shouldSetAllFieldsCorrectly() {
        // Arrange
        Loan loan1 = new Loan();
        Loan loan2 = new Loan();
        List<Loan> loanList = List.of(loan1, loan2);

        LoanStatus loanStatus = new LoanStatus();

        // Act
        loanStatus.setId(1L);
        loanStatus.setDescription("OVERDUE");
        loanStatus.setLoans(loanList);

        // Assert
        assertThat(loanStatus.getId()).isEqualTo(1L);
        assertThat(loanStatus.getDescription()).isEqualTo("OVERDUE");
        assertThat(loanStatus.getLoans()).containsExactly(loan1, loan2);
    }

    @Test
    void testToString_shouldContainIdAndDescription() {
        LoanStatus loanStatus = new LoanStatus("RETURNED");
        loanStatus.setId(99L);

        String result = loanStatus.toString();

        assertThat(result).contains("LoanStatus{");
        assertThat(result).contains("id=99");
        assertThat(result).contains("description='RETURNED'");
    }
}
