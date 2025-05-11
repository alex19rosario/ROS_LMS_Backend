package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.entities.Member;
import com.ros.lms.domain.entities.User;
import com.ros.lms.domain.exceptions.EmailAlreadyExistsException;
import com.ros.lms.domain.exceptions.MemberAlreadyExistsException;
import com.ros.lms.domain.exceptions.UsernameAlreadyExistsException;
import com.ros.lms.ports.outbound.repository_contracts.MemberDAO;
import com.ros.lms.ports.outbound.repository_contracts.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    @Mock
    private MemberDAO memberDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    private AddMemberDTO validMemberDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample AddMemberDTO
        validMemberDTO = new AddMemberDTO(
                "123123123",
                "carlos alexander",
                "rosario sanchez",
                "6474256438",
                (byte) 27,
                'M',
                "test19@gmail.com",
                "carlos19",
                "test123"
        );
    }

    @Test
    void add_Success() throws MemberAlreadyExistsException, UsernameAlreadyExistsException, EmailAlreadyExistsException {
        // Mock the DAO responses
        when(memberDAO.findByGovernmentID(validMemberDTO.governmentID())).thenReturn(Optional.empty());
        when(memberDAO.findByUsername(validMemberDTO.username())).thenReturn(Optional.empty());
        when(memberDAO.findByEmail(validMemberDTO.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validMemberDTO.password())).thenReturn("hashedPassword");

        // Execute the method
        memberService.add(validMemberDTO);

        // Verify the interactions
        verify(memberDAO).findByGovernmentID(validMemberDTO.governmentID());
        verify(memberDAO).findByUsername(validMemberDTO.username());
        verify(memberDAO).findByEmail(validMemberDTO.email());
        verify(userDAO).create(any(User.class));
        verify(memberDAO).create(any(Member.class));
    }


    @Test
    void add_ThrowsMemberAlreadyExistsException() {
        when(memberDAO.findByGovernmentID(validMemberDTO.governmentID())).thenReturn(Optional.of(new Member()));

        assertThrows(MemberAlreadyExistsException.class, () -> memberService.add(validMemberDTO));

        verify(memberDAO).findByGovernmentID(validMemberDTO.governmentID());
        verify(memberDAO, never()).findByUsername(anyString());
        verify(memberDAO, never()).findByEmail(anyString());
        verify(userDAO, never()).create(any());
        verify(memberDAO, never()).create(any());
    }

    @Test
    void add_ThrowsUsernameAlreadyExistsException() {
        when(memberDAO.findByGovernmentID(validMemberDTO.governmentID())).thenReturn(Optional.empty());
        when(memberDAO.findByUsername(validMemberDTO.username())).thenReturn(Optional.of(new Member()));

        assertThrows(UsernameAlreadyExistsException.class, () -> memberService.add(validMemberDTO));

        verify(memberDAO).findByGovernmentID(validMemberDTO.governmentID());
        verify(memberDAO).findByUsername(validMemberDTO.username());
        verify(memberDAO, never()).findByEmail(anyString());
        verify(userDAO, never()).create(any());
        verify(memberDAO, never()).create(any());
    }

    @Test
    void add_ThrowsEmailAlreadyExistsException() {
        when(memberDAO.findByGovernmentID(validMemberDTO.governmentID())).thenReturn(Optional.empty());
        when(memberDAO.findByUsername(validMemberDTO.username())).thenReturn(Optional.empty());
        when(memberDAO.findByEmail(validMemberDTO.email())).thenReturn(Optional.of(new Member()));

        assertThrows(EmailAlreadyExistsException.class, () -> memberService.add(validMemberDTO));

        verify(memberDAO).findByGovernmentID(validMemberDTO.governmentID());
        verify(memberDAO).findByUsername(validMemberDTO.username());
        verify(memberDAO).findByEmail(validMemberDTO.email());
        verify(userDAO, never()).create(any());
        verify(memberDAO, never()).create(any());
    }




}
