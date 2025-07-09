package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.Loan;
import com.ros.lms.ports.outbound.repository_contracts.LoanDAO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LoanDAOJpaImpl implements LoanDAO {

    private final EntityManager entityManager;

    @Autowired
    public LoanDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(Loan loan) {
        this.entityManager.persist(loan);
    }
}
