package com.ros.lms.adapters.inbound.controllers;

import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.exceptions.*;
import com.ros.lms.ports.inbound.service_contracts.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(@Qualifier("loanServiceImpl") LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/loans")
    public void issueBook(@RequestBody AddLoanDTO addLoanDTO) throws
            BookNotFoundException,
            BookNotAvailableException,
            MemberNotFoundException,
            StaffNotFoundException,
            MemberHasActiveLoanException,
            MemberHasOverdueLoanException
    {
        loanService.add(addLoanDTO);
    }
}
