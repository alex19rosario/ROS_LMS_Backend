package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.dtos.ReturnBookDTO;
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

import java.time.LocalDateTime;

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
    public Loan add(AddLoanDTO addLoanDTO) throws BookNotFoundException, BookNotAvailableException, MemberNotFoundException, StaffNotFoundException, MemberHasActiveLoanException, MemberHasOverdueLoanException {

        //Check if book exists
        Book book = bookDAO.findByISBN(addLoanDTO.bookIsbn())
                .orElseThrow(() -> new BookNotFoundException("Book with ISBN " + addLoanDTO.bookIsbn() + " not found."));

        //Check if book is available
        if (!book.isAvailable()) throw new BookNotAvailableException("Book with ISBN " + addLoanDTO.bookIsbn() + " not available.");

        //Check if member exists
        Member member = memberDAO.findByUsername(addLoanDTO.memberUsername())
                .orElseThrow(() -> new MemberNotFoundException("Member with username " + addLoanDTO.memberUsername() + " not found"));

        // Check if staff exists
        Staff staff = staffDAO.findByUsername(addLoanDTO.staffUsername())
                .orElseThrow(() -> new StaffNotFoundException("Staff with username " + addLoanDTO.staffUsername() + " not found."));

        // Check if member is eligible
        MemberStatuses memberStatus = memberStatusDAO.findStatusByMemberId(member.getId())
                .orElseThrow(() -> new IllegalStateException("Could not determine member status for member ID " + member.getId()));

        // The loan object must be created outside the switch in order to be returned
        Loan loan = new Loan();

        switch (memberStatus) {
            case OVERDUE -> throw new MemberHasOverdueLoanException("Member has an overdue loan.");
            case HAS_LOAN -> throw new MemberHasActiveLoanException("Member already has an active loan.");
            case ELIGIBLE -> {
                // Proceed to create loan
                LoanStatus loanStatus = loanStatusDAO.findLoanStatusByEnum(LoanStatuses.LOANED)
                        .orElseThrow(() -> new IllegalStateException("Could not determine loan status for member ID " + member.getId()));
                book.setAvailable(false);
                loan = new Loan(member, book, loanStatus, staff);
                loanDAO.create(loan);
                bookDAO.update(book);
            }
        }

        return loan;
    }

    @Transactional
    @CacheEvict(value = "booksCache", allEntries = true)
    @Override
    public Loan returnBook(ReturnBookDTO returnBookDTO) throws BookNotRegisteredException, BookAlreadyInStockException, InvalidStaffException {

        //Check if the book is registered in the system, and if a member actually has it checked out
        Book book = bookDAO.findByISBN(returnBookDTO.isbn())
                .orElseThrow(() -> new BookNotRegisteredException(
                        "No book found with ISBN (" + returnBookDTO.isbn() + "). It may not belong to this library."
                ));

        if (book.isAvailable()) {
            throw new BookAlreadyInStockException(
                    "The book with ISBN (" + returnBookDTO.isbn() + ") is already available in stock; no member currently has it checked out."
            );
        }
        // Validate staff member
        Staff staff = staffDAO.findByUsername(returnBookDTO.staffUsername())
                .orElseThrow(() -> new InvalidStaffException(
                        "No valid staff member found with username (" + returnBookDTO.staffUsername() + ")."
                ));

        // Find the active loan
        Loan loan = loanDAO.findActiveLoanByBook(book)
                .orElseThrow(() -> new IllegalStateException(
                        "No active loan found for the book with ISBN (" + returnBookDTO.isbn() + ")."
                ));

        // Update loan: set return date and status
        LocalDateTime now = LocalDateTime.now();
        loan.setReturnDate(now);

        LoanStatuses newStatus = loan.getDueDate().isBefore(now)
                ? LoanStatuses.RETURNED_LATE
                : LoanStatuses.RETURNED;

        // Assuming you have a method to fetch LoanStatus by code
        LoanStatus status = loanStatusDAO.findLoanStatusByEnum(newStatus)
                .orElseThrow(() -> new IllegalStateException(
                        "Loan status (" + newStatus + ") not found in database."
                ));

        loan.setStatus(status);

        // Update book availability
        book.setAvailable(true);

        // Persist changes
        bookDAO.update(book);
        loanDAO.update(loan);

        return loan ;
    }
}
