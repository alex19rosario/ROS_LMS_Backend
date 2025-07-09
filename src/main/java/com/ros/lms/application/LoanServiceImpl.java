package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.entities.*;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.domain.enums.MemberStatuses;
import com.ros.lms.domain.exceptions.*;
import com.ros.lms.ports.inbound.service_contracts.LoanService;
import com.ros.lms.ports.outbound.repository_contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanServiceImpl implements LoanService {

    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final LoanStatusDAO loanStatusDAO;
    private final LoanDAO loanDAO;
    private final StaffDAO staffDAO;
    private final MemberStatusDAO memberStatusDAO;

    @Autowired
    public LoanServiceImpl(
            @Qualifier("bookDAOJpaImpl") BookDAO bookDAO,
            @Qualifier("memberDAOJpaImpl") MemberDAO memberDAO,
            @Qualifier("loanStatusDAOJpaImpl") LoanStatusDAO loanStatusDAO,
            @Qualifier("loanDAOJpaImpl") LoanDAO loanDAO,
            @Qualifier("staffDAOJpaImpl") StaffDAO staffDAO,
            @Qualifier("memberStatusDAOJpaImpl") MemberStatusDAO memberStatusDAO) {
        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
        this.loanStatusDAO = loanStatusDAO;
        this.loanDAO = loanDAO;
        this.staffDAO = staffDAO;
        this.memberStatusDAO = memberStatusDAO;
    }

    @Transactional
    @CacheEvict(value = "booksCache", allEntries = true)
    @Override
    public void add(AddLoanDTO addLoanDTO) throws BookNotFoundException, BookNotAvailableException, MemberNotFoundException, StaffNotFoundException, MemberHasActiveLoanException, MemberHasOverdueLoanException {

        //Check if book exists
        Book book = bookDAO.findById(addLoanDTO.bookId())
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + addLoanDTO.bookId() + " not found."));

        //Check if book is available
        if (!book.isAvailable()) throw new BookNotAvailableException("Book with ID " + addLoanDTO.bookId() + " not available.");

        //Check if member exists
        Member member = memberDAO.findByUsername(addLoanDTO.memberUsername())
                .orElseThrow(() -> new MemberNotFoundException("Member with username " + addLoanDTO.memberUsername() + " not found"));

        // Check if staff exists
        Staff staff = staffDAO.findByUsername(addLoanDTO.staffUsername())
                .orElseThrow(() -> new StaffNotFoundException("Staff with username " + addLoanDTO.staffUsername() + " not found."));

        // Check if member is eligible
        MemberStatuses memberStatus = memberStatusDAO.findStatusByMemberId(member.getId())
                .orElseThrow(() -> new IllegalStateException("Could not determine member status for member ID " + member.getId()));

        switch (memberStatus) {
            case OVERDUE -> throw new MemberHasOverdueLoanException("Member has an overdue loan.");
            case HAS_LOAN -> throw new MemberHasActiveLoanException("Member already has an active loan.");
            case ELIGIBLE -> {
                // Proceed to create loan
                LoanStatus loanStatus = loanStatusDAO.findLoanStatusByEnum(LoanStatuses.LOANED)
                        .orElseThrow(() -> new IllegalStateException("Could not determine loan status for member ID " + member.getId()));
                book.setAvailable(false);
                Loan loan = new Loan(member, book, loanStatus, staff);
                loanDAO.create(loan);
                bookDAO.update(book);
            }
        }
    }
}
