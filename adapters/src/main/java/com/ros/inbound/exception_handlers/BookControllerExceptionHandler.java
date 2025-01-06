package com.ros.inbound.exception_handlers;

import com.ros.exceptions.BookAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.net.URI;

@ControllerAdvice
public class BookControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleBookAlreadyExistException(BookAlreadyExistException ex, HttpServletRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Existing Book Error");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleGeneralException(RuntimeException ex, HttpServletRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Internal Error");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
