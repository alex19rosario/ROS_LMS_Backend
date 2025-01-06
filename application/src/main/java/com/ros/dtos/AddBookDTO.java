package com.ros.dtos;

import java.util.Set;

public record AddBookDTO (long ISBN, String title, Set<AuthorDTO> authors, Set<String> genres) {
}
