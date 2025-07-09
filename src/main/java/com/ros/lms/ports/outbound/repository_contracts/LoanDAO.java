package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Loan;

public interface LoanDAO {
    void create(Loan loan);
}
