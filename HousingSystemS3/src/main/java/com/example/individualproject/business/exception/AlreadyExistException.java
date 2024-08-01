package com.example.individualproject.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AlreadyExistException extends ResponseStatusException {
    public AlreadyExistException() {
        super(HttpStatus.BAD_REQUEST, "ALREADY_EXIST");
    }

    public AlreadyExistException(String errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}
