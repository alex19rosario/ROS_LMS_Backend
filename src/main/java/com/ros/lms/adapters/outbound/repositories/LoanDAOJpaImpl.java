package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.Loan;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.ports.outbound.repository_contracts.LoanDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Loan> findActiveLoanByBook(Book book) {
        List<LoanStatuses> activeStatuses = List.of(
                LoanStatuses.LOANED,
                LoanStatuses.OVERDUE,
                LoanStatuses.LOST
        );

        TypedQuery<Loan> query = entityManager.createQuery(
                "SELECT l FROM Loan l WHERE l.book = :book AND l.status.code IN :statuses",
                Loan.class
        );
        query.setParameter("book", book);
        query.setParameter("statuses", activeStatuses);

        return query.getResultStream().findFirst();
    }

    @Override
    public void update(Loan loan) {
        entityManager.merge(loan);
    }
}
