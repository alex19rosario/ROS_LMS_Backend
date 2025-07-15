package com.ros.lms.ports.inbound.service_contracts;


import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.BookDTO;
import com.ros.lms.domain.dtos.SearchBookDTO;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;
import com.ros.lms.domain.exceptions.PageOutOfRangeException;
import com.ros.lms.domain.exceptions.StaffNotFoundException;
import com.ros.lms.domain.exceptions.StorageException;
import org.springframework.data.domain.Page;

public interface BookService {
    void add(AddBookDTO dto) throws BookAlreadyExistsException, StorageException, StaffNotFoundException;
    Page<BookDTO> getAll(SearchBookDTO searchBookDTO) throws PageOutOfRangeException;
}
