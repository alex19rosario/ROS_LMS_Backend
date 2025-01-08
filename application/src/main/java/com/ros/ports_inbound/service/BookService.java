package com.ros.ports_inbound.service;

import com.ros.dtos.AddBookDTO;
import com.ros.exceptions.BookAlreadyExistsException;

public interface BookService {
    void add(AddBookDTO dto) throws BookAlreadyExistsException;
}
