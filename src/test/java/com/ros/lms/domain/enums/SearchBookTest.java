package com.ros.lms.domain.enums;

import com.ros.lms.domain.dtos.SearchBookDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SearchBookTest {

    @Test
    void toString_shouldIncludeAllFieldsWhenPresent() {
        SearchBookDTO dto = new SearchBookDTO(
                1,
                20,
                "Clean Code",
                GenreType.TECHNOLOGY,
                "Robert",
                "Martin",
                true
        );

        String result = dto.toString();

        assertThat(result).isEqualTo("page=1&size=20&title=Clean Code&genre=TECHNOLOGY&authorFirstName=Robert&authorLastName=Martin&isAvailable=true");
    }

    @Test
    void toString_shouldHandleNullOptionalFields() {
        SearchBookDTO dto = new SearchBookDTO(
                0,
                10,
                null,
                null,
                null,
                null,
                null
        );

        String result = dto.toString();

        assertThat(result).isEqualTo("page=0&size=10&title=&genre=&authorFirstName=&authorLastName=&isAvailable=");
    }

    @Test
    void toString_shouldHandleMixedNullAndNonNullFields() {
        SearchBookDTO dto = new SearchBookDTO(
                2,
                15,
                "Design Patterns",
                null,
                "Erich",
                null,
                false
        );

        String result = dto.toString();

        assertThat(result).isEqualTo("page=2&size=15&title=Design Patterns&genre=&authorFirstName=Erich&authorLastName=&isAvailable=false");
    }
}
