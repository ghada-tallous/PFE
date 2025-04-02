package com.example.Pfe.service;


import com.example.Pfe.dto.JwtAuthentificationResponse;
import com.example.Pfe.dto.RefreshTokenRequest;
import com.example.Pfe.dto.SignInRequest;
import com.example.Pfe.dto.SignUpRequest;
import com.example.Pfe.entites.User;

public interface AuthenticationService {
    JwtAuthentificationResponse signUp(SignUpRequest signUpRequest);
    JwtAuthentificationResponse signIn(SignInRequest signInRequest);
    JwtAuthentificationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}

