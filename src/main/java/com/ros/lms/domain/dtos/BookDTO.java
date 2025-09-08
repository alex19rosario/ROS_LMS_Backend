package com.ros.lms.domain.dtos;

import com.ros.lms.domain.enums.GenreType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * @description DTO for searching books
 * @param id
 * @param isbn
 * @param title
 * @param authors
 * @param genres
 * @param status
 * @param imagePath
 */

public record BookDTO(
        long id,

        @NotBlank(message = "ISBN is required")
        @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters long")
        String isbn,

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,

        @NotEmpty(message = "At least one author is required")
        Set<AuthorDTO> authors,

        @NotEmpty(message = "At least one genre is required")
        Set<GenreType> genres,

        // status = true (available) or false (not available), no validation needed
        boolean status,

        // Optional: path to cover image, may be null or empty
        String imagePath
) { }
