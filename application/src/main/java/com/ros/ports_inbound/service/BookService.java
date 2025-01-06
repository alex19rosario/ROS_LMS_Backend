package com.ros.ports_inbound.service;

import com.ros.dtos.AddBookDTO;
import com.ros.exceptions.BookAlreadyExistException;

public interface BookService {
    void save(AddBookDTO dto) throws BookAlreadyExistException;
}
