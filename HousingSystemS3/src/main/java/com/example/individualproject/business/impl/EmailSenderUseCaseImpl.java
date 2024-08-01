package com.example.individualproject.business.impl;

import com.example.individualproject.business.EmailSenderUseCase;
import com.example.individualproject.business.exception.FailedSendEmailException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@AllArgsConstructor
public class EmailSenderUseCaseImpl implements EmailSenderUseCase {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try{
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            throw new FailedSendEmailException();
        }
    }
}
