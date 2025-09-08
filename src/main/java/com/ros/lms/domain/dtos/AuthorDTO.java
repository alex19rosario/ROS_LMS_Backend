package com.ros.lms.domain.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * @description DTO for author
 * @param firstName
 * @param lastName
 */

public record AuthorDTO(
        @NotBlank(message = "Author's first name cannot be blank")
        String firstName,

        @NotBlank(message = "Author's last name cannot be blank")
        String lastName
) { }
