package com.ros.lms.domain.dtos;

import com.ros.lms.domain.enums.GenreType;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for searching books using pagination and optional filters.
 *
 * @param page            Page number (0-based)
 * @param size            Page size
 * @param title           (Optional) Book title filter
 * @param genre           (Optional) Genre filter
 * @param authorFirstName (Optional) Author's first name filter
 * @param authorLastName  (Optional) Author's last name filter
 * @param isAvailable     (Optional) Filter by availability
 */
public record SearchBookDTO(
        @NotNull
        int page,
        @NotNull
        int size,
        String title,
        GenreType genre,
        String authorFirstName,
        String authorLastName,
        Boolean isAvailable
) {
    @Override
    public String toString() {
        return "page=" + page +
                "&size=" + size +
                "&title=" + (title != null ? title : "") +
                "&genre=" + (genre != null ? genre.name() : "") +
                "&authorFirstName=" + (authorFirstName != null ? authorFirstName : "") +
                "&authorLastName=" + (authorLastName != null ? authorLastName : "") +
                "&isAvailable=" + (isAvailable != null ? isAvailable : "");
    }
}
