package com.ros.lms.domain.dtos;

import com.ros.lms.domain.enums.Sex;

import java.time.LocalDate;

/**
 * @description DTO for adding a member
 * @param governmentID
 * @param firstName
 * @param lastName
 * @param phone
 * @param dateOfBirth
 * @param sex
 * @param email
 * @param username
 * @param password
 */
public record AddMemberDTO(String governmentID,
                           String firstName,
                           String lastName,
                           String phone,
                           LocalDate dateOfBirth,
                           Sex sex,
                           String email,
                           String username,
                           String password) {
}
