package com.ros.lms.ports.inbound.service_contracts;


import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.BookDTO;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;
import com.ros.lms.domain.exceptions.PageOutOfRangeException;
import com.ros.lms.domain.exceptions.StorageException;
import org.springframework.data.domain.Page;

public interface BookService {
    void add(AddBookDTO dto) throws BookAlreadyExistsException, StorageException;
    Page<BookDTO> getAll(int page, int size, String title, String genre, String authorFirstName, String authorLastName) throws PageOutOfRangeException;
}
