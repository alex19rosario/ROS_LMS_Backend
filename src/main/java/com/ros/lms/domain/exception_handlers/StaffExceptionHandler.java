package com.ros.lms.domain.exception_handlers;

import com.ros.lms.domain.exceptions.InvalidStaffException;
import com.ros.lms.domain.exceptions.StaffNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class StaffExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleStaffNotFoundException(StaffNotFoundException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Staff not Found");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleInvalidStaffException(InvalidStaffException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setTitle("Invalid Staff");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }
}
