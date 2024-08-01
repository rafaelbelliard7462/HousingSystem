package com.example.individualproject.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExist extends ResponseStatusException {
    public EmailAlreadyExist() {super(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXISTS");
    }

}
