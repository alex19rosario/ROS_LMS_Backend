package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.entities.AuthorityType;
import com.ros.lms.domain.entities.Member;
import com.ros.lms.domain.entities.Staff;
import com.ros.lms.domain.entities.User;
import com.ros.lms.domain.enums.MemberValidationStatus;
import com.ros.lms.domain.enums.RoleType;
import com.ros.lms.domain.enums.Sex;
import com.ros.lms.domain.exceptions.*;
import com.ros.lms.ports.outbound.repository_contracts.AuthorityTypeDAO;
import com.ros.lms.ports.outbound.repository_contracts.MemberDAO;
import com.ros.lms.ports.outbound.repository_contracts.StaffDAO;
import com.ros.lms.ports.outbound.repository_contracts.UserDAO;
import com.ros.lms.util.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    @Mock
    private MemberDAO memberDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private StaffDAO staffDAO;

    @Mock
    private AuthorityTypeDAO authorityTypeDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    private AddMemberDTO validMemberDTO;
    private AddMemberDTO validMemberDtoWithMiddleName;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mapper mapper = new Mapper();
        memberService = new MemberServiceImpl(memberDAO, userDAO, staffDAO, authorityTypeDAO, passwordEncoder, mapper);

        // Create a sample AddMemberDTO
        validMemberDtoWithMiddleName = new AddMemberDTO(
                "123123123",
                "carlos alexander",
                "rosario sanchez",
                "6474256438",
                LocalDate.of(1985, 9, 15),
                Sex.MALE,
                "test19@gmail.com",
                "carlos19",
                "test123",
                "staff"
        );

        validMemberDTO = new AddMemberDTO(
                "123123987",
                "Peter",
                "Zeus",
                "6474259589",
                LocalDate.of(1999, 7, 22),
                Sex.MALE,
                "test18@gmail.com",
                "peter27",
                "test123",
                "staff"
        );
    }

    @Test
    void add_ShouldCreateMember_WhenNoConflicts() throws MemberValidationException, StaffNotFoundException {
        when(memberDAO.validateMemberUniqueness(any(), any(), any())).thenReturn(Set.of());
        when(staffDAO.findByUsername(validMemberDTO.staffUsername())).thenReturn(Optional.of(new Staff()));
        when(authorityTypeDAO.findByLabel(RoleType.MEMBER))
                .thenReturn(Optional.of(new AuthorityType(RoleType.MEMBER)));

        memberService.add(validMemberDTO);

        verify(memberDAO).validateMemberUniqueness("123123987", "peter27", "test18@gmail.com");
        verify(userDAO).create(any(User.class));
        verify(memberDAO).create(any(Member.class));
    }

    @Test
    void add_ShouldThrow_WhenGovernmentIdExists() {
        when(memberDAO.validateMemberUniqueness(any(), any(), any()))
                .thenReturn(EnumSet.of(MemberValidationStatus.GOVERNMENT_ID_EXISTS));

        MemberValidationException ex = assertThrows(MemberValidationException.class,
                () -> memberService.add(validMemberDTO));

        assertTrue(ex.getMessage().contains("Government ID"));
        verify(userDAO, never()).create(any());
        verify(memberDAO, never()).create(any());
    }

    @Test
    void add_ShouldThrow_WhenUsernameExists() {
        when(memberDAO.validateMemberUniqueness(any(), any(), any()))
                .thenReturn(EnumSet.of(MemberValidationStatus.USERNAME_EXISTS));

        MemberValidationException ex = assertThrows(MemberValidationException.class,
                () -> memberService.add(validMemberDTO));

        assertTrue(ex.getMessage().contains("Username"));
        verify(userDAO, never()).create(any());
        verify(memberDAO, never()).create(any());
    }

    @Test
    void add_ShouldThrow_WhenEmailExists() {
        when(memberDAO.validateMemberUniqueness(any(), any(), any()))
                .thenReturn(EnumSet.of(MemberValidationStatus.EMAIL_EXISTS));

        MemberValidationException ex = assertThrows(MemberValidationException.class,
                () -> memberService.add(validMemberDTO));

        assertTrue(ex.getMessage().contains("Email"));
        verify(userDAO, never()).create(any());
        verify(memberDAO, never()).create(any());
    }

    @Test
    void add_ShouldThrow_WhenMultipleConflictsExist() {
        when(memberDAO.validateMemberUniqueness(any(), any(), any()))
                .thenReturn(EnumSet.of(
                        MemberValidationStatus.GOVERNMENT_ID_EXISTS,
                        MemberValidationStatus.USERNAME_EXISTS,
                        MemberValidationStatus.EMAIL_EXISTS));

        MemberValidationException ex = assertThrows(MemberValidationException.class,
                () -> memberService.add(validMemberDTO));

        assertTrue(ex.getMessage().contains("Government ID"));
        assertTrue(ex.getMessage().contains("Username"));
        assertTrue(ex.getMessage().contains("Email"));
        verify(userDAO, never()).create(any());
        verify(memberDAO, never()).create(any());
    }

    @Test
    void add_ShouldThrow_WhenStaffNotFound() {
        when(memberDAO.validateMemberUniqueness(any(), any(), any())).thenReturn(Set.of());
        when(staffDAO.findByUsername("staff")).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> memberService.add(validMemberDTO));

        verify(userDAO, never()).create(any());
        verify(memberDAO, never()).create(any());
    }

    @Test
    void add_ShouldThrow_WhenRoleNotFound() {
        when(memberDAO.validateMemberUniqueness(any(), any(), any())).thenReturn(Set.of());
        when(staffDAO.findByUsername(validMemberDTO.staffUsername())).thenReturn(Optional.of(new Staff()));
        when(authorityTypeDAO.findByLabel(RoleType.MEMBER)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> memberService.add(validMemberDTO));

        verify(userDAO, never()).create(any());
        verify(memberDAO, never()).create(any());
    }
}
