package com.ros.lms.ports.inbound.service_contracts;

import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.exceptions.*;

public interface LoanService {
    void add(AddLoanDTO addLoanDTO) throws BookNotFoundException, BookNotAvailableException, MemberNotFoundException,
            StaffNotFoundException, MemberHasActiveLoanException, MemberHasOverdueLoanException;
}
