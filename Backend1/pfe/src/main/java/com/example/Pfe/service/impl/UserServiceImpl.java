package com.example.Pfe.service.impl;


import com.example.Pfe.entites.User;
import com.example.Pfe.repository.UserRepository;
import com.example.Pfe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;




@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
                // Retourne un UserDetails construit à partir de l'entité User
                return org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name()) // Assurez-vous que Role est un enum ou une entité avec un nom
                        .build();
            }
        };
    }

    // Générer un token de réinitialisation sécurisé
    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString(); // Token unique
        user.setResetToken(token);
        user.setResetTokenExpiryDate(LocalDateTime.now().plusHours(1)); // Expire dans 1 heure
        userRepository.save(user); // Ensure the user is saved with the new token
        return token;
    }

    // Valider le token
    public boolean validatePasswordResetToken(String token) {
        User user = userRepository.findByResetToken(token)
                .orElse(null);
        if (user == null || user.getResetTokenExpiryDate() == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(user.getResetTokenExpiryDate());
    }

    // Réinitialiser le mot de passe
    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElse(null);
        if (user == null || !validatePasswordResetToken(token)) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // Invalider le token après utilisation
        user.setResetTokenExpiryDate(null);
        userRepository.save(user);
        return true;
    }

    public boolean confirmEmail(String token) {
        var user = userRepository.findByConfirmationToken(token).orElse(null);
        if (user != null) {
            user.setEmailConfirmed(true);
            user.setConfirmationToken(null); // Supprime le token après confirmation
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /*@Override
    public User saveUtilisateur(User u) {

        return userRepository.save(u);

    }*/

    @Override
    public User getUtilisateur(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUtilisateurByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> getAllUtilisateurs() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUtilisateur(User u) {

        return userRepository.save(u);
    }
    @Override
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null);
    }
}
