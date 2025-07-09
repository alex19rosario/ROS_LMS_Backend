package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.LoanStatus;
import com.ros.lms.domain.enums.LoanStatuses;

import java.util.Optional;

public interface LoanStatusDAO {
    Optional<LoanStatus> findLoanStatusByEnum(LoanStatuses status);
}
