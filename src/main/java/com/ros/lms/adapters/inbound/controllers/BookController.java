package com.ros.lms.adapters.inbound.controllers;

import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;
import com.ros.lms.domain.exceptions.StorageException;
import com.ros.lms.ports.inbound.service_contracts.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(@Qualifier("bookServiceImpl") BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/books", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addBook(
            @RequestParam("ISBN") long ISBN,
            @RequestParam("title") String title,
            @RequestParam("authors") String authors,
            @RequestParam("genres") String genres,
            @RequestParam("coverImage") MultipartFile coverImage
    ) throws BookAlreadyExistsException, StorageException {
        AddBookDTO addBookDTO = new AddBookDTO(ISBN, title, authors, genres, coverImage);
        bookService.add(addBookDTO);
    }


}
