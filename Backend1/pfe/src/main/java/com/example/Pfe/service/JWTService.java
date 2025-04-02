package com.example.Pfe.service;


import com.example.Pfe.dto.JwtAuthentificationResponse;
import com.example.Pfe.dto.RefreshTokenRequest;
import com.example.Pfe.entites.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JWTService {
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(Map<String, Object> extraClaims, User user);
    String extractUserName(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}

