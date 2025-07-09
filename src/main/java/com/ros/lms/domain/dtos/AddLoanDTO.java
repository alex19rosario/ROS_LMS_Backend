package com.ros.lms.domain.dtos;

/**
 * @description DTO for issuing a book
 * @param bookId
 * @param memberUsername
 * @param staffUsername
 */
public record AddLoanDTO(long bookId,
                         String memberUsername,
                         String staffUsername) {
}
