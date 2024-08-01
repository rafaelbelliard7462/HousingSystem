package com.example.individualproject.business.impl;

import com.example.individualproject.business.exception.FailedSendEmailException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderUseCaseImplTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderUseCaseImpl emailSenderUseCase;

    @Test
    void sendEmail_Successful()  {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        emailSenderUseCase.sendEmail("test@example.com", "Test Subject", "Test Body");

        // Verify interactions
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendEmail_Failed() {

        assertThrows(FailedSendEmailException.class, () ->
                emailSenderUseCase.sendEmail("test@example.com", "Test Subject", "Test Body"));

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }


}