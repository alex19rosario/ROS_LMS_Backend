package com.ros.inbound.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/")
    public String sayHello(){
        return "Hello World";
    }

    @DeleteMapping("/books/{bookId}")
    public String deleteBook(@PathVariable long bookId){
        return "The book associated to the id " + bookId + " has been successfully deleted";
    }

}
