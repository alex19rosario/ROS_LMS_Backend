package com.ros.lms.domain.dtos;

import org.springframework.web.multipart.MultipartFile;

/**
 * @description DTO for adding a book
 * @param isbn
 * @param title
 * @param authors
 * @param genres
 * @param coverImage
 */
public record AddBookDTO (String isbn,
                          String title,
                          String authors,
                          String genres,
                          MultipartFile coverImage) {
}
