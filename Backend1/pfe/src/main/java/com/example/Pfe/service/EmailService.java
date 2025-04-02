package com.example.Pfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        String resetLink = "http://localhost:4200/reset-password?token=" + token; // Lien pour le frontend
        String subject = "Réinitialisation de votre mot de passe";
        String body = "<h2>Réinitialisation de mot de passe</h2>"
                + "<p>Vous avez demandé à réinitialiser votre mot de passe. Cliquez sur le lien ci-dessous pour procéder :</p>"
                + "<p><a href=\"" + resetLink + "\">Réinitialiser mon mot de passe</a></p>"
                + "<p>Ce lien expire dans 1 heure. Si vous n'avez pas demandé cette réinitialisation, ignorez cet email.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true pour HTML

        mailSender.send(message);
    }
}