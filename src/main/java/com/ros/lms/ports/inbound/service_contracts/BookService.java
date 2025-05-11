package com.ros.lms.ports.inbound.service_contracts;


import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;

public interface BookService {
    void add(AddBookDTO dto) throws BookAlreadyExistsException;
}
