package com.ros.dtos;

/**
 * @description DTO for adding a member
 * @param governmentID
 * @param firstName
 * @param lastName
 * @param phone
 * @param age
 * @param sex
 * @param email
 * @param username
 * @param password
 */
public record AddMemberDTO(String governmentID,
                           String firstName,
                           String lastName,
                           String phone,
                           byte age,
                           char sex,
                           String email,
                           String username,
                           String password) {
}
