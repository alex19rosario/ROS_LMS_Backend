package com.ros.lms.ports.inbound.service_contracts;

import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.dtos.ReturnBookDTO;
import com.ros.lms.domain.entities.Loan;
import com.ros.lms.domain.exceptions.*;

public interface LoanService {
    void add(AddLoanDTO addLoanDTO) throws BookNotFoundException, BookNotAvailableException, MemberNotFoundException,
            StaffNotFoundException, MemberHasActiveLoanException, MemberHasOverdueLoanException;

    Loan returnBook(ReturnBookDTO returnBookDTO) throws BookNotRegisteredException, BookAlreadyInStockException, InvalidStaffException;
}
