package com.ros.lms.domain.exceptions;

public class BookAlreadyExistsException extends Exception {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
