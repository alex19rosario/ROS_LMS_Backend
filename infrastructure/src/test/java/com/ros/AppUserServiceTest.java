package com.ros;

import com.ros.entities.Authority;
import com.ros.entities.AuthorityId;
import com.ros.entities.User;
import com.ros.user_service.AppUserService;
import com.ros.user_service.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AppUserService appUserService;

    private User user;

    @BeforeEach
    void setup() {
        // Initialize a User instance for testing
        Authority authority = new Authority();
        AuthorityId authorityId = new AuthorityId();
        authorityId.setUsername("testUser");
        authorityId.setAuthority("ROLE_MEMBER");
        authority.setId(authorityId);

        user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setEnabled('Y');
        user.setAuthorities(Set.of(authority));
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        when(userDAO.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = appUserService.loadUserByUsername("testUser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MEMBER")));
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());

        verify(userDAO, times(1)).findByUsername("testUser");
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userDAO.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> appUserService.loadUserByUsername("nonExistentUser")
        );
        assertEquals("User not found", exception.getMessage());
        verify(userDAO, times(1)).findByUsername("nonExistentUser");
    }

    @Test
    void loadUserByUsername_UserDisabled_ReturnsUserDetailsWithDisabledFlag() {
        // Arrange
        user.setEnabled('N');
        when(userDAO.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = appUserService.loadUserByUsername("testUser");

        // Assert
        assertNotNull(userDetails);
        assertFalse(userDetails.isEnabled());
        verify(userDAO, times(1)).findByUsername("testUser");
    }
}
