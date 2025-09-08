package com.ros.lms.domain.dtos;

import com.ros.lms.domain.enums.Sex;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * @description DTO for adding a member
 * @param governmentID
 * @param firstName
 * @param lastName
 * @param phone
 * @param dateOfBirth
 * @param sex
 * @param email
 * @param username
 * @param password
 * @param staffUsername
 */
public record AddMemberDTO(
        @NotBlank(message = "Government ID is required")
        String governmentID,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Phone is required")
        String phone,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotNull(message = "Sex is required")
        Sex sex,

        @NotNull
        @Email
        String email,

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 5, message = "Password must be at least 5 characters long")
        String password,

        @NotBlank(message = "Staff username is required")
        String staffUsername
) { }
