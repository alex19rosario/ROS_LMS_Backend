package com.ros.lms.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    private Author author;
    private final String testFirstName = "John";
    private final String testMiddleName = "Ronald";
    private final String testLastName = "Tolkien";

    @BeforeEach
    void setUp() {
        author = new Author(testFirstName, testMiddleName, testLastName);
    }

    @Test
    void testGettersAndSetters() {
        // Test initial values from constructor
        assertEquals(testFirstName, author.getFirstName());
        assertEquals(testMiddleName, author.getMiddleName());
        assertEquals(testLastName, author.getLastName());
        assertThat(author.getBooks()).isEmpty();

        // Test setters
        long newId = 1L;
        author.setId(newId);
        assertEquals(newId, author.getId());

        String newFirstName = "George";
        author.setFirstName(newFirstName);
        assertEquals(newFirstName, author.getFirstName());

        String newMiddleName = "Raymond";
        author.setMiddleName(newMiddleName);
        assertEquals(newMiddleName, author.getMiddleName());

        String newLastName = "Martin";
        author.setLastName(newLastName);
        assertEquals(newLastName, author.getLastName());

        List<Book> books = new ArrayList<>();
        books.add(new Book("9780007136556", "The Lord of the Rings", true));
        author.setBooks(books);

        assertNotNull(author.getBooks());
        assertEquals(1, author.getBooks().size());
        assertEquals("The Lord of the Rings", author.getBooks().getFirst().getTitle());
    }

    @Test
    void testConstructors() {
        // Test minimal constructor
        Author minimalAuthor = new Author(testFirstName, testMiddleName, testLastName);
        assertEquals(testFirstName, minimalAuthor.getFirstName());
        assertEquals(testMiddleName, minimalAuthor.getMiddleName());
        assertEquals(testLastName, minimalAuthor.getLastName());
        assertThat(minimalAuthor.getBooks()).isEmpty();

        // Test full constructor
        List<Book> books = List.of(
                new Book("9780007136556", "The Lord of the Rings", true),
                new Book("9780261102354", "The Hobbit", true)
        );
        Author fullAuthor = new Author(testFirstName, testMiddleName, testLastName, books);

        assertEquals(testFirstName, fullAuthor.getFirstName());
        assertEquals(testMiddleName, fullAuthor.getMiddleName());
        assertEquals(testLastName, fullAuthor.getLastName());
        assertEquals(2, fullAuthor.getBooks().size());
        assertEquals("The Hobbit", fullAuthor.getBooks().get(1).getTitle());
    }

    @Test
    void testBooksManagement() {
        // Initially should be null
        assertThat(author.getBooks()).isEmpty();

        // Test setting books list
        List<Book> books = new ArrayList<>();
        books.add(new Book("9780007136556", "The Lord of the Rings", true));
        author.setBooks(books);

        assertNotNull(author.getBooks());
        assertEquals(1, author.getBooks().size());
        assertEquals("The Lord of the Rings", author.getBooks().getFirst().getTitle());

        // Test modifying the books list
        author.getBooks().add(new Book("9780261102354", "The Hobbit", true));
        assertEquals(2, author.getBooks().size());
    }

    @Test
    void testNullNames() {
        author.setFirstName(null);
        assertNull(author.getFirstName());

        author.setMiddleName(null);
        assertNull(author.getMiddleName());

        author.setLastName(null);
        assertNull(author.getLastName());
    }

    @Test
    void testEmptyNames() {
        author.setFirstName("");
        assertEquals("", author.getFirstName());

        author.setMiddleName(" ");
        assertEquals(" ", author.getMiddleName());

        author.setLastName("\t");
        assertEquals("\t", author.getLastName());
    }

    @Test
    void testSetBooksToNull() {
        List<Book> books = List.of(new Book("9780007136556", "The Lord of the Rings", true));
        author.setBooks(books);
        assertNotNull(author.getBooks());

        author.setBooks(null);
        assertNull(author.getBooks());
    }

    @Test
    void testToString() {
        // Test with no books
        String toStringResult = author.toString();
        assertTrue(toStringResult.contains("firstName='" + testFirstName + "'"));
        assertTrue(toStringResult.contains("middleName='" + testMiddleName + "'"));
        assertTrue(toStringResult.contains("lastName='" + testLastName + "'"));

        // Verify books are not included in toString()
        assertFalse(toStringResult.contains("books="));
    }

    @Test
    void testPartialNameConstructor() {
        Author authorWithoutMiddle = new Author("J.K.", null, "Rowling");
        assertEquals("J.K.", authorWithoutMiddle.getFirstName());
        assertNull(authorWithoutMiddle.getMiddleName());
        assertEquals("Rowling", authorWithoutMiddle.getLastName());
    }

    @Test
    void testEmptyBooksList() {
        author.setBooks(new ArrayList<>());
        assertNotNull(author.getBooks());
        assertTrue(author.getBooks().isEmpty());
    }


}
