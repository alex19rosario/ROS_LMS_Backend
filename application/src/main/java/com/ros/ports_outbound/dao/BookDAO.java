package com.ros.ports_outbound.dao;


import com.ros.entities.Book;

import java.util.Optional;

public interface BookDAO {
    void create(Book book);
    Optional<Book> findByISBN(long ISBN);
}
