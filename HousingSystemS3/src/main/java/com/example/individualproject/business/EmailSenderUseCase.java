package com.example.individualproject.business;


public interface EmailSenderUseCase {
    void sendEmail(String to, String subject, String body);
}
