package com.ros.inbound.controllers;

import com.ros.dtos.AddBookDTO;
import com.ros.exceptions.BookAlreadyExistsException;
import com.ros.ports_inbound.service.BookService;
import com.ros.ports_inbound.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(@Qualifier("bookServiceImpl") BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/books")
    public void addBook(@RequestBody AddBookDTO addBookDTO) throws BookAlreadyExistsException {
        bookService.add(addBookDTO);
    }


}
