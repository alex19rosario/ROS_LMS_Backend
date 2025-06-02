package com.ros.lms.domain.dtos;

import org.springframework.web.multipart.MultipartFile;

/**
 * @description DTO for adding a book
 * @param ISBN
 * @param title
 * @param authors
 * @param genres
 * @param coverImage
 */
public record AddBookDTO (long ISBN,
                          String title,
                          String authors,
                          String genres,
                          MultipartFile coverImage) {
}
