package com.example.individualproject.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FailedSendEmailException extends ResponseStatusException {
    public FailedSendEmailException(){
        super(HttpStatus.BAD_REQUEST, "FAILED_TO_SEND_EMAIL");
    }
}
