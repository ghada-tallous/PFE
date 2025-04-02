package com.example.Pfe.service.impl;


import com.example.Pfe.dto.JwtAuthentificationResponse;
import com.example.Pfe.dto.RefreshTokenRequest;
import com.example.Pfe.dto.SignInRequest;
import com.example.Pfe.dto.SignUpRequest;
import com.example.Pfe.entites.Role;
import com.example.Pfe.entites.User;
import com.example.Pfe.repository.UserRepository;
import com.example.Pfe.service.AuthenticationService;
import com.example.Pfe.service.JWTService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final JavaMailSender mailSender; // Injecte JavaMailSender

    @Override
    public JwtAuthentificationResponse signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.COLLABORATOR);
        user.setEmailConfirmed(false); // L'email n'est pas encore confirmé
        user.setConfirmationToken(UUID.randomUUID().toString()); // Génère un token de confirmation

        // Sauvegarde l'utilisateur
        User savedUser = userRepository.save(user);
        // Envoie un email de confirmation
        try {
            sendConfirmationEmail(savedUser); // Envoie l'email
        } catch (RuntimeException e) {
            // En cas d'échec, supprimez l'utilisateur ou gérez l'erreur
            userRepository.delete(savedUser);
            throw new RuntimeException("Échec de l'envoi de l'email de confirmation : " + e.getMessage());
        }
        //générer un token
        String token = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), savedUser);

        JwtAuthentificationResponse response = new JwtAuthentificationResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);

        return response;
    }

    /*private void sendConfirmationEmail(User user) {
        String confirmationLink = "http://localhost:8080/api/v1/auth/confirm?token=" + user.getConfirmationToken();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirmez votre adresse email");
        message.setText("Merci de vous être inscrit ! Veuillez confirmer votre adresse email en cliquant sur ce lien : " + confirmationLink);
        mailSender.send(message);
    }*/
    /*private void sendConfirmationEmail(User user) {
        String confirmationLink = "http://localhost:8080/api/v1/auth/confirm?token=" + user.getConfirmationToken();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Bienvenue chez nous - Confirmez votre email");
            helper.setText("<p>Cliquez ici : <a href='" + confirmationLink + "'>Confirmer</a></p>", true);
            helper.setFrom("votre.email@gmail.com");
            mailSender.send(message);
            System.out.println("Email envoyé à : " + user.getEmail());
        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de l'email de confirmation : " + e.getMessage());
        }
    }*/
    private void sendConfirmationEmail(User user) {
        String confirmationLink = "http://localhost:8080/api/v1/auth/confirm?token=" + user.getConfirmationToken();
        String subject = "Bienvenue chez nous - Confirmez votre email";

        // Contenu HTML amélioré
        String htmlBody = "<!DOCTYPE html>" +
                "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>" +
                "<h2 style='color: #2A5582;'>Bienvenue, " + user.getFirstName() + " !</h2>" +
                "<p>Merci de vous être inscrit(e) sur notre plateforme. Pour activer votre compte, veuillez confirmer votre adresse email en cliquant sur le bouton ci-dessous :</p>" +
                "<p style='text-align: center;'>" +
                "<a href='" + confirmationLink + "' style='display: inline-block; padding: 10px 20px; background-color: #2A5582; color: white; text-decoration: none; border-radius: 5px;'>Confirmer mon email</a>" +
                "</p>" +
                "<p>Ce lien est valide pendant <strong>24 heures</strong>. Si vous ne confirmez pas dans ce délai, vous devrez peut-être demander un nouveau lien.</p>" +
                "<p>Si vous n'avez pas initié cette inscription, ignorez simplement cet email ou contactez notre support à <a href='mailto:support@votre-domaine.com'>support@votre-domaine.com</a>.</p>" +
                "<p>À bientôt,<br>L'équipe de [Nom de votre application]</p>" +
                "<hr style='border: 0; border-top: 1px solid #eee;'>" +
                "<p style='font-size: 12px; color: #999;'>Vous recevez cet email car vous vous êtes inscrit sur [Nom de votre application].</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indique que le contenu est HTML
            helper.setFrom("votre.email@gmail.com"); // Remplacez par votre email

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de confirmation : " + e.getMessage());
        }
    }

    @Override
    public JwtAuthentificationResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(user.isEmailConfirmed()) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthentificationResponse response = new JwtAuthentificationResponse();
        response.setToken(jwt);
        response.setRefreshToken(refreshToken);
            return response;
        }else{
            throw new RuntimeException("Email not confirmed");
        }
    }


    public JwtAuthentificationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);
            JwtAuthentificationResponse response = new JwtAuthentificationResponse();
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            return response;
        }
        return null;
    }

}
