package com.ros.lms.domain.dtos;

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
        long isbn,
        String title,
        Set<AuthorDTO> authors,
        Set<String> genres,
        boolean status,
        String imagePath
) {
}
