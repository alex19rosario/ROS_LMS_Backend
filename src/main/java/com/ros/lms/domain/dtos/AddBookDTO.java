package com.ros.lms.domain.dtos;

import java.util.Set;

/**
 * @description DTO for adding a book
 * @param ISBN
 * @param title
 * @param authors
 * @param genres
 */
public record AddBookDTO (long ISBN, String title, Set<AuthorDTO> authors, Set<String> genres) {
}
