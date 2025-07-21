package com.ros.lms.ports.inbound.service_contracts;


import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.BookDTO;
import com.ros.lms.domain.dtos.SearchBookDTO;
import com.ros.lms.domain.exceptions.*;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface BookService {
    void add(AddBookDTO dto) throws BookAlreadyExistsException, StorageException, StaffNotFoundException;
    Page<BookDTO> getAll(SearchBookDTO searchBookDTO) throws PageOutOfRangeException;
    Optional<BookDTO> getByIsbn(String isbn) throws BookNotFoundException;
}
