package com.backend.esports.backend_esports.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destino) {

        SimpleMailMessage mensaje =
            new SimpleMailMessage();

        mensaje.setTo(destino);
        mensaje.setSubject("Verificación de cuenta");
        String link =
    "http://localhost:8080/auth/verify?email=" + destino;

    mensaje.setText(
    "Haz click para verificar tu cuenta:\n\n" + link
);

        mailSender.send(mensaje);

        System.out.println("Email enviado a: " + destino);
    }
}