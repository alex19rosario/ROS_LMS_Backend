package com.ros.lms.domain.exceptions;

public class MemberAlreadyExistsException extends Exception {
    public MemberAlreadyExistsException(String message) {
        super(message);
    }
}
