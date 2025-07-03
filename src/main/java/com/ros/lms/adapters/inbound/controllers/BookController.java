package com.ros.lms.adapters.inbound.controllers;

import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.BookDTO;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;
import com.ros.lms.domain.exceptions.PageOutOfRangeException;
import com.ros.lms.domain.exceptions.StorageException;
import com.ros.lms.ports.inbound.service_contracts.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam("isbn") long isbn,
            @RequestParam("title") String title,
            @RequestParam("authors") String authors,
            @RequestParam("genres") String genres,
            @RequestParam("coverImage") MultipartFile coverImage
    ) throws BookAlreadyExistsException, StorageException {
        AddBookDTO addBookDTO = new AddBookDTO(isbn, title, authors, genres, coverImage);
        bookService.add(addBookDTO);
    }

    @GetMapping("/books")
    public PagedModel<EntityModel<BookDTO>> getAllBooks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String genre,
        @RequestParam(required = false) String authorFirstName,
        @RequestParam(required = false) String authorLastName,
        PagedResourcesAssembler<BookDTO> assembler
    ) throws PageOutOfRangeException {
        Page<BookDTO> booksPage = bookService.getAll(page, size, title, genre, authorFirstName, authorLastName);
        return assembler.toModel(booksPage, EntityModel::of);
    }
}
