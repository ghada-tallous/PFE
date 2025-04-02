package com.example.Pfe.controller;


import com.example.Pfe.dto.PasswordResetConfirmRequest;
import com.example.Pfe.dto.PasswordResetRequest;
import com.example.Pfe.service.EmailService;
import com.example.Pfe.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class PasswordResetController {

    private final UserServiceImpl userService;
    private final EmailService emailService;

    // Demander un token de réinitialisation et envoyer l'email
    @PostMapping("/forgot-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            String token = userService.createPasswordResetToken(request.getEmail());
            emailService.sendPasswordResetEmail(request.getEmail(), token);
            return ResponseEntity.ok("Un lien de réinitialisation a été envoyé à votre email.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }

    // Valider un token (optionnel, pour une page web)
    @GetMapping("/reset-password")
    public ResponseEntity<String> showResetPasswordPage(@RequestParam("token") String token) {
        if (userService.validatePasswordResetToken(token)) {
            return ResponseEntity.ok("Token is valid. You can now reset your password.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }

    // Confirmer la réinitialisation du mot de passe
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> confirmResetPassword(
            @RequestParam("token") String token,
            @RequestBody PasswordResetConfirmRequest request) {
        if (userService.resetPassword(token, request.getNewPassword())) {
            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token or token has expired.");
        }
    }
}
