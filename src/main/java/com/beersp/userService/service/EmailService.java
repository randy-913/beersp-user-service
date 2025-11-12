package com.beersp.userService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String username) {
        String asunto = "Verifica tu cuenta en BeerSP";
        String verificationLink = "http://localhost:8080/v1/auth/verify?correo=" + to;

        String texto = String.format(
            "¡Hola %s!\n\nGracias por registrarte en BeerSP.\n\nHaz clic en el siguiente enlace para activar tu cuenta:\n%s\n\n", 
            username, verificationLink
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(asunto);
        message.setText(texto);
        mailSender.send(message);
    }
}
