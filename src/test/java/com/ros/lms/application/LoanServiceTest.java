package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.LoanStatus;
import com.ros.lms.domain.entities.Member;
import com.ros.lms.domain.entities.Staff;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.domain.enums.MemberStatuses;
import com.ros.lms.domain.exceptions.*;
import com.ros.lms.ports.outbound.repository_contracts.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoanServiceTest {

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

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        validLoanDTO = new AddLoanDTO(1L, "memberUser", "staffUser");
        availableBook = new Book(1L, "Test Book", true);
        validMember = new Member();
        validMember.setId(10L);
        loanedStatus = new LoanStatus("LOANED");
        loanedStatus.setId(1L);
        validStaff = new Staff();
        validStaff.setId(11L);
    }

    @Test
    void add_shouldThrowBookNotFoundException_whenBookDoesNotExist() {
        when(bookDAO.findById(validLoanDTO.bookId())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> loanService.add(validLoanDTO));
        verify(bookDAO).findById(validLoanDTO.bookId());
    }

    @Test
    void add_shouldThrowBookNotAvailableException_whenBookIsNotAvailable() {
        availableBook.setAvailable(false);
        when(bookDAO.findById(validLoanDTO.bookId())).thenReturn(Optional.of(availableBook));
        assertThrows(BookNotAvailableException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowMemberNotFoundException_whenMemberNotFound() {
        when(bookDAO.findById(validLoanDTO.bookId())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.empty());
        assertThrows(MemberNotFoundException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowStaffNotFoundException_whenStaffNotFound() {
        when(bookDAO.findById(validLoanDTO.bookId())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.empty());
        assertThrows(StaffNotFoundException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowIllegalStateException_whenMemberStatusNotFound() {
        when(bookDAO.findById(validLoanDTO.bookId())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowMemberHasOverdueLoanException_whenMemberStatusIsOverdue() {
        when(bookDAO.findById(validLoanDTO.bookId())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.of(MemberStatuses.OVERDUE));
        assertThrows(MemberHasOverdueLoanException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowMemberHasActiveLoanException_whenMemberStatusIsHasLoan() {
        when(bookDAO.findById(validLoanDTO.bookId())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.of(MemberStatuses.HAS_LOAN));
        assertThrows(MemberHasActiveLoanException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldThrowIllegalStateException_whenLoanStatusNotFound() {
        when(bookDAO.findById(validLoanDTO.bookId())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.of(MemberStatuses.ELIGIBLE));
        when(loanStatusDAO.findLoanStatusByEnum(LoanStatuses.LOANED)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> loanService.add(validLoanDTO));
    }

    @Test
    void add_shouldCreateLoan_whenAllValidationsPass() throws Exception {
        when(bookDAO.findById(validLoanDTO.bookId())).thenReturn(Optional.of(availableBook));
        when(memberDAO.findByUsername(validLoanDTO.memberUsername())).thenReturn(Optional.of(validMember));
        when(staffDAO.findByUsername(validLoanDTO.staffUsername())).thenReturn(Optional.of(validStaff));
        when(memberStatusDAO.findStatusByMemberId(validMember.getId())).thenReturn(Optional.of(MemberStatuses.ELIGIBLE));
        when(loanStatusDAO.findLoanStatusByEnum(LoanStatuses.LOANED)).thenReturn(Optional.of(loanedStatus));

        loanService.add(validLoanDTO);

        verify(loanDAO).create(any());
        verify(bookDAO).update(any(Book.class));
    }

}
