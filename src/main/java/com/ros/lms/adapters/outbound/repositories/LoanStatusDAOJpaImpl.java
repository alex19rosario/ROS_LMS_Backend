package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.LoanStatus;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.ports.outbound.repository_contracts.LoanStatusDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LoanStatusDAOJpaImpl implements LoanStatusDAO {

    private final EntityManager entityManager;

    @Autowired
    public LoanStatusDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<LoanStatus> findLoanStatusByEnum(LoanStatuses status) {
        String query = "SELECT ls FROM LoanStatus ls WHERE ls.description = :description";
        try {
            LoanStatus loanStatus = entityManager.createQuery(query, LoanStatus.class)
                    .setParameter("description", status.getVal())
                    .getSingleResult();

            return Optional.ofNullable(loanStatus);

        } catch (NoResultException e){
            return Optional.empty();
        }
    }
}
