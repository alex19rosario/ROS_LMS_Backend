package com.ros.lms.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @description DTO for returning a book
 * @param isbn
 * @param staffUsername
 */
public record ReturnBookDTO(
        @NotBlank(message = "ISBN is required")
        @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters long")
        String isbn,

        @NotBlank(message = "Staff username is required")
        String staffUsername) {
}
