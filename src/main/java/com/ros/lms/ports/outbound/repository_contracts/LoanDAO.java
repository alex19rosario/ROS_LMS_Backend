package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.Loan;

import java.util.Optional;

public interface LoanDAO {
    void create(Loan loan);
    Optional<Loan> findActiveLoanByBook(Book book);
    void update(Loan loan);
}
