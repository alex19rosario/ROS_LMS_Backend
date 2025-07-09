package com.ros.lms.domain.exceptions;

public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
