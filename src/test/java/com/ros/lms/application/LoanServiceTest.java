package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.dtos.ReturnBookDTO;
import com.ros.lms.domain.entities.*;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.domain.enums.MemberStatuses;
import com.ros.lms.domain.exceptions.*;
import com.ros.lms.ports.outbound.repository_contracts.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoanServiceTest {

    @Mock private BookDAO bookDAO;
    @Mock private MemberDAO memberDAO;
    @Mock private LoanStatusDAO loanStatusDAO;
    @Mock private LoanDAO loanDAO;
    @Mock private StaffDAO staffDAO;
    @Mock private MemberStatusDAO memberStatusDAO;

    @InjectMocks
    private LoanServiceImpl loanService;

    private AddLoanDTO validLoanDTO;
    private Book availableBook;
    private Member validMember;
    private LoanStatus loanedStatus;
    private Staff validStaff;

    private ReturnBookDTO validReturnBookDTO;
    private Loan activeLoan;
    private LoanStatus returnedStatus;
    private LoanStatus returnedLateStatus;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        validLoanDTO = new AddLoanDTO("1234567895", "memberUser", "staffUser");
        availableBook = new Book("1234567895", "Test Book", true);
        validMember = new Member();
        validMember.setId(10L);
        loanedStatus = new LoanStatus(LoanStatuses.LOANED);
        loanedStatus.setId(1L);
        validStaff = new Staff();
        validStaff.setId(11L);

        validReturnBookDTO = new ReturnBookDTO("1234567895", "staffUser");

        activeLoan = new Loan();
        activeLoan.setDueDate(LocalDateTime.now().plusDays(1)); // not overdue by default

        returnedStatus = new LoanStatus(LoanStatuses.RETURNED);
        returnedStatus.setId(2L);

        returnedLateStatus = new LoanStatus(LoanStatuses.RETURNED_LATE);
        returnedLateStatus.setId(3L);
    }

    @Test
    void add_shouldThrowBookNotFoundException_whenBookDoesNotExist() {
        when(bookDAO.findByISBN(validLoanDTO.bookIsbn())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> loanService.add(validLoanDTO));
        verify(bookDAO).findByISBN(validLoanDTO.bookIsbn());
    }

    @Test
    void add_shouldThrowBookNotAvailableException_whenBookIsNotAvailable() {
        availableBook.setAvailable(false);
        when(bookDAO.findByISBN(validLoanDTO.bookIsbn())).thenReturn(Optional.of(availableBook));
        assertThrows(BookNotAvailableException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowMemberNotFoundException_whenMemberNotFound() {
        when(bookDAO.findByISBN(validLoanDTO.bookIsbn())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.empty());
        assertThrows(MemberNotFoundException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowStaffNotFoundException_whenStaffNotFound() {
        when(bookDAO.findByISBN(validLoanDTO.bookIsbn())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.empty());
        assertThrows(StaffNotFoundException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowIllegalStateException_whenMemberStatusNotFound() {
        when(bookDAO.findByISBN(validLoanDTO.bookIsbn())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowMemberHasOverdueLoanException_whenMemberStatusIsOverdue() {
        when(bookDAO.findByISBN(validLoanDTO.bookIsbn())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.of(MemberStatuses.OVERDUE));
        assertThrows(MemberHasOverdueLoanException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowMemberHasActiveLoanException_whenMemberStatusIsHasLoan() {
        when(bookDAO.findByISBN(validLoanDTO.bookIsbn())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.of(MemberStatuses.HAS_LOAN));
        assertThrows(MemberHasActiveLoanException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowIllegalStateException_whenLoanStatusNotFound() {
        when(bookDAO.findByISBN(validLoanDTO.bookIsbn())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.of(MemberStatuses.ELIGIBLE));
        when(loanStatusDAO.findLoanStatusByEnum(LoanStatuses.LOANED)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldCreateLoan_whenAllValidationsPass() throws Exception {
        when(bookDAO.findByISBN(validLoanDTO.bookIsbn())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.of(MemberStatuses.ELIGIBLE));
        when(loanStatusDAO.findLoanStatusByEnum(LoanStatuses.LOANED)).thenReturn(Optional.of(loanedStatus));

        loanService.add(validLoanDTO);

        verify(loanDAO).create(any());
        verify(bookDAO).update(any(Book.class));
    }

    @Test
    void returnBook_shouldThrowBookNotRegisteredException_whenBookNotFound() {
        when(bookDAO.findByISBN(validReturnBookDTO.isbn())).thenReturn(Optional.empty());

        assertThrows(BookNotRegisteredException.class, () -> loanService.returnBook(validReturnBookDTO));
        verify(bookDAO).findByISBN(validReturnBookDTO.isbn());
    }

    @Test
    void returnBook_shouldThrowBookAlreadyInStockException_whenBookAlreadyAvailable() {
        availableBook.setAvailable(true);
        when(bookDAO.findByISBN(validReturnBookDTO.isbn())).thenReturn(Optional.of(availableBook));

        assertThrows(BookAlreadyInStockException.class, () -> loanService.returnBook(validReturnBookDTO));
    }

    @Test
    void returnBook_shouldThrowInvalidStaffException_whenStaffNotFound() {
        availableBook.setAvailable(false);
        when(bookDAO.findByISBN(validReturnBookDTO.isbn())).thenReturn(Optional.of(availableBook));
        when(staffDAO.findByUsername(validReturnBookDTO.staffUsername())).thenReturn(Optional.empty());

        assertThrows(InvalidStaffException.class, () -> loanService.returnBook(validReturnBookDTO));
    }

    @Test
    void returnBook_shouldThrowIllegalStateException_whenNoActiveLoanFound() {
        availableBook.setAvailable(false);
        when(bookDAO.findByISBN(validReturnBookDTO.isbn())).thenReturn(Optional.of(availableBook));
        when(staffDAO.findByUsername(validReturnBookDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(loanDAO.findActiveLoanByBook(availableBook)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> loanService.returnBook(validReturnBookDTO));
    }

    @Test
    void returnBook_shouldThrowIllegalStateException_whenLoanStatusNotFound() {
        availableBook.setAvailable(false);
        when(bookDAO.findByISBN(validReturnBookDTO.isbn())).thenReturn(Optional.of(availableBook));
        when(staffDAO.findByUsername(validReturnBookDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(loanDAO.findActiveLoanByBook(availableBook)).thenReturn(Optional.of(activeLoan));
        when(loanStatusDAO.findLoanStatusByEnum(any())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> loanService.returnBook(validReturnBookDTO));
    }

    @Test
    void returnBook_shouldUpdateLoanAndBook_whenReturnedOnTime() throws Exception {
        availableBook.setAvailable(false);
        when(bookDAO.findByISBN(validReturnBookDTO.isbn())).thenReturn(Optional.of(availableBook));
        when(staffDAO.findByUsername(validReturnBookDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(loanDAO.findActiveLoanByBook(availableBook)).thenReturn(Optional.of(activeLoan));
        when(loanStatusDAO.findLoanStatusByEnum(LoanStatuses.RETURNED)).thenReturn(Optional.of(returnedStatus));

        loanService.returnBook(validReturnBookDTO);

        verify(bookDAO).update(availableBook);
        verify(loanDAO).update(activeLoan);
    }

    @Test
    void returnBook_shouldSetReturnedLateStatus_whenBookIsOverdue() throws Exception {
        availableBook.setAvailable(false);
        activeLoan.setDueDate(LocalDateTime.now().minusDays(1)); // overdue
        when(bookDAO.findByISBN(validReturnBookDTO.isbn())).thenReturn(Optional.of(availableBook));
        when(staffDAO.findByUsername(validReturnBookDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(loanDAO.findActiveLoanByBook(availableBook)).thenReturn(Optional.of(activeLoan));
        when(loanStatusDAO.findLoanStatusByEnum(LoanStatuses.RETURNED_LATE)).thenReturn(Optional.of(returnedLateStatus));

        loanService.returnBook(validReturnBookDTO);

        verify(bookDAO).update(availableBook);
        verify(loanDAO).update(activeLoan);
    }

}
