package com.ros.dtos;

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
