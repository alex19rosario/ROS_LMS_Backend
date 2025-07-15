package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.enums.GenreType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookDAO {
    void create(Book book);
    Optional<Book> findByISBN(String isbn);
    Page<Book> findAllOrderedByTitle(String title, GenreType genre, String authorFirstName, String authorLastName, Boolean isAvailable, Pageable pageable);
    Optional<Book> findById(long id);
    void update(Book book);
}
