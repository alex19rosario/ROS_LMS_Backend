package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.GenreType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    private Genre genre;
    private final GenreType testDescription = GenreType.SCIENCE_FICTION;

    @BeforeEach
    void setUp() {
        genre = new Genre(testDescription);
    }

    @Test
    void testGettersAndSetters() {
        // Test initial values from constructor
        assertEquals(testDescription, genre.getLabel());
        assertThat(genre.getBooks()).isEmpty();

        // Test setters
        long newId = 1L;
        genre.setId(newId);
        assertEquals(newId, genre.getId());

        genre.setLabel(GenreType.FANTASY);
        assertEquals(GenreType.FANTASY, genre.getLabel());

        List<Book> books = new ArrayList<>();
        books.add(new Book("9783161484100", "Dune", true));
        genre.setBooks(books);

        assertNotNull(genre.getBooks());
        assertEquals(1, genre.getBooks().size());
        assertEquals("Dune", genre.getBooks().getFirst().getTitle());
    }

    @Test
    void testConstructors() {
        // Test minimal constructor
        Genre minimalGenre = new Genre(testDescription);
        assertEquals(testDescription, minimalGenre.getLabel());
        assertThat(minimalGenre.getBooks()).isEmpty();

        // Test full constructor
        List<Book> books = List.of(
                new Book("9783161484100", "Dune", true),
                new Book("9780553103540", "Game of Thrones", true)
        );
        Genre fullGenre = new Genre(testDescription, books);

        assertEquals(testDescription, fullGenre.getLabel());
        assertEquals(2, fullGenre.getBooks().size());
        assertEquals("Dune", fullGenre.getBooks().getFirst().getTitle());
    }

    @Test
    void testBooksManagement() {
        // Initially should be null
        assertThat(genre.getBooks()).isEmpty();

        // Test setting books list
        List<Book> books = new ArrayList<>();
        books.add(new Book("9783161484100", "Dune", true));
        genre.setBooks(books);

        assertNotNull(genre.getBooks());
        assertEquals(1, genre.getBooks().size());
        assertEquals("Dune", genre.getBooks().getFirst().getTitle());

        // Test modifying the books list
        genre.getBooks().add(new Book("9780553103540", "Game of Thrones", true));
        assertEquals(2, genre.getBooks().size());
    }

    @Test
    void testSetBooksToNull() {
        List<Book> books = List.of(new Book("9783161484100", "Dune", true));
        genre.setBooks(books);
        assertNotNull(genre.getBooks());

        genre.setBooks(null);
        assertNull(genre.getBooks());
    }

    @Test
    void testToString() {
        // Test with no books
        String toStringResult = genre.toString();
        assertTrue(toStringResult.contains("label='" + testDescription.getVal() + "'"));
        assertTrue(toStringResult.contains("books=[]"));

        // Test with books
        List<Book> books = List.of(new Book("9783161484100", "Dune", true));
        genre.setBooks(books);
        toStringResult = genre.toString();
        assertTrue(toStringResult.contains("books=" + books.toString()));
    }
}
