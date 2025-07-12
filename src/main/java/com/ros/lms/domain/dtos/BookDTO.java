package com.ros.lms.domain.dtos;

import com.ros.lms.domain.enums.GenreType;

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
        String isbn,
        String title,
        Set<AuthorDTO> authors,
        Set<GenreType> genres,
        boolean status,
        String imagePath
) {
}
