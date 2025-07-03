package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookDAO {
    void create(Book book);
    Optional<Book> findByISBN(long isbn);
    Page<Book> findAllOrderedByTitle(String title, String genre, String authorFirstName, String authorLastName, Pageable pageable);
}
