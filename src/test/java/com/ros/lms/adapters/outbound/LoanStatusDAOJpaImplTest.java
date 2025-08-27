package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.LoanStatusDAOJpaImpl;
import com.ros.lms.domain.entities.LoanStatus;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.ports.outbound.repository_contracts.LoanStatusDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LoanStatusDAOJpaImplTest {

    @Autowired
    private EntityManager entityManager;

    private LoanStatusDAO loanStatusDAO;

    @BeforeEach
    void setUp() {
        loanStatusDAO = new LoanStatusDAOJpaImpl(entityManager);
    }

    @Test
    @Transactional
    void findLoanStatusByEnum_shouldReturnCorrectStatus_whenExists() {
        // Arrange
        LoanStatus loanStatus = new LoanStatus(LoanStatuses.LOANED);
        entityManager.persist(loanStatus);

        // Act
        Optional<LoanStatus> result = loanStatusDAO.findLoanStatusByEnum(LoanStatuses.LOANED);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo(LoanStatuses.LOANED);
    }

    @Test
    @Transactional
    void findLoanStatusByEnum_shouldReturnEmpty_whenStatusNotFound() {
        // Act
        Optional<LoanStatus> result = loanStatusDAO.findLoanStatusByEnum(LoanStatuses.LOST);

        // Assert
        assertThat(result).isEmpty();
    }

}
