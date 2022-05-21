package com.dinesh.project.service;

import com.dinesh.project.dto.Email;
import com.dinesh.project.exception.ClientErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class NotificationService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${spring.mail.username}")
    private String username;


    private final JavaMailSender mailSender;

    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(Email email) {
        try {
            System.out.println("email = " + email);
            System.out.println("fromEmail = " + fromEmail);
            System.out.println("username = " + username);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromEmail);
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody(), true);
            mailSender.send(message);

        }catch (MessagingException e) {
            throw new ClientErrorException(e.getMessage());
        }
    }

}
