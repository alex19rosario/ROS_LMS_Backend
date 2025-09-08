package com.ros.lms.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description DTO for adding a book
 * @param isbn
 * @param title
 * @param authors
 * @param genres
 * @param staffUsername
 * @param coverImage
 */
public record AddBookDTO (
        @NotBlank(message = "ISBN is required")
        @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters long")
        String isbn,

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Authors field is required")
        String authors,

        @NotBlank(message = "Genres field is required")
        String genres,

        @NotBlank(message = "Staff username is required")
        String staffUsername,

        MultipartFile coverImage
) {
}
