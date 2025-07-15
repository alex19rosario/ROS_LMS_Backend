package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.GenreType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    private Book book;
    private final String testISBN = "9783161484100";
    private final String testTitle = "Effective Java";
    private final boolean available = true;
    private final String coverImagePath = "upload-dir/test.png";

    @BeforeEach
    void setUp() {
        book = new Book(testISBN, testTitle, available);
    }

    @Test
    void testGettersAndSetters() {
        // Test initial values from constructor
        assertEquals(testISBN, book.getIsbn());
        assertEquals(testTitle, book.getTitle());
        assertEquals(available, book.isAvailable());

        // Test setters
        long newId = 1L;
        book.setId(newId);
        assertEquals(newId, book.getId());

        String newISBN = "9780201633610";
        book.setIsbn(newISBN);
        assertEquals(newISBN, book.getIsbn());

        String newTitle = "Design Patterns";
        book.setTitle(newTitle);
        assertEquals(newTitle, book.getTitle());

        book.setAvailable(false);
        assertFalse(book.isAvailable());

        book.setCoverImagePath(coverImagePath);
        assertEquals(coverImagePath, book.getCoverImagePath());
    }

    @Test
    void testAuthorsManagement() {
        // Initially should be null
        assertNull(book.getAuthors());

        // Test adding single author
        Author author1 = new Author();
        author1.setFirstName("Joshua");
        author1.setLastName("Bloch");
        book.addAuthor(author1);

        assertNotNull(book.getAuthors());
        assertEquals(1, book.getAuthors().size());
        assertEquals("Joshua", book.getAuthors().getFirst().getFirstName());

        // Test setting authors list
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Erich", "", "Gamma"));
        authors.add(new Author("Richard", "", "Helm"));

        book.setAuthors(authors);

        assertEquals(2, book.getAuthors().size());
        assertEquals("Gamma", book.getAuthors().getFirst().getLastName());
    }

    @Test
    void testGenresManagement() {
        // Initially should be null
        assertNull(book.getGenres());

        // Test adding single genre
        Genre genre1 = new Genre();
        genre1.setDescription(GenreType.TECHNOLOGY);
        book.addGenre(genre1);

        assertNotNull(book.getGenres());
        assertEquals(1, book.getGenres().size());
        assertEquals(GenreType.TECHNOLOGY, book.getGenres().getFirst().getDescription());

        // Test setting genres list
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(GenreType.SCIENCE));
        genres.add(new Genre(GenreType.ADVENTURE));
        book.setGenres(genres);

        assertEquals(2, book.getGenres().size());
        assertEquals(GenreType.SCIENCE, book.getGenres().getFirst().getDescription());
    }

    @Test
    void testConstructors() {
        // Test minimal constructor
        Book minimalBook = new Book(testISBN, testTitle, available);
        assertEquals(testISBN, minimalBook.getIsbn());
        assertEquals(testTitle, minimalBook.getTitle());
        assertEquals(available, minimalBook.isAvailable());
        assertNull(minimalBook.getAuthors());
        assertNull(minimalBook.getGenres());
        assertNull(minimalBook.getCoverImagePath());

        // Test full constructor
        List<Author> authors = List.of(new Author("Joshua", "", "Bloch"));
        List<Genre> genres = List.of(new Genre(GenreType.TECHNOLOGY));
        Book fullBook = new Book(testISBN, testTitle, available, coverImagePath, authors, genres);

        assertEquals(authors, fullBook.getAuthors());
        assertEquals(genres, fullBook.getGenres());
        assertEquals(coverImagePath, fullBook.getCoverImagePath());
    }

    @Test
    void testAddAuthorToNullList() {
        book.setAuthors(null); // Explicitly set to null
        book.addAuthor(new Author("Martin", "", "Fowler"));
        assertNotNull(book.getAuthors());
        assertEquals(1, book.getAuthors().size());
    }

    @Test
    void testAddGenreToNullList() {
        book.setGenres(null); // Explicitly set to null
        book.addGenre(new Genre(GenreType.TECHNOLOGY));
        assertNotNull(book.getGenres());
        assertEquals(1, book.getGenres().size());
    }

    @Test
    void testToString() {
        String toStringResult = book.toString();
        assertTrue(toStringResult.contains("ISBN=" + testISBN));
        assertTrue(toStringResult.contains("title='" + testTitle + "'"));
        assertTrue(toStringResult.contains("isAvailable=" + available));
    }
}
