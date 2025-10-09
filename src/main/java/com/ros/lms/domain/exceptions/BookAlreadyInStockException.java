package com.ros.lms.domain.exceptions;

public class BookAlreadyInStockException extends Exception {
    public BookAlreadyInStockException(String message) {
        super(message);
    }
}
