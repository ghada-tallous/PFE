package com.example.Pfe.service;


import com.example.Pfe.entites.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();

   // User saveUtilisateur(User u);
    User getUtilisateur(Long id);
    User getUtilisateurByEmail(String email);
    void deleteById(Long id);
    List<User> getAllUtilisateurs();
    User updateUtilisateur(User u);

    User findByEmail(String email); // Ajoute cette méthode
}