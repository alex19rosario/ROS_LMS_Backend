package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Book;

import java.util.Optional;

public interface BookDAO {
    void create(Book book);
    Optional<Book> findByISBN(long ISBN);
}
