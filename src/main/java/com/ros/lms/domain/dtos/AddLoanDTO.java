package com.ros.lms.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @description DTO for issuing a book
 * @param bookId
 * @param memberUsername
 * @param staffUsername
 */
public record AddLoanDTO(
        @NotNull
        Long bookId,

        @NotBlank(message = "Member username is required")
        String memberUsername,

        @NotBlank(message = "Staff username is required")
        String staffUsername
) { }
