package com.example.Pfe.dto;


import lombok.Data;

@Data
public class JwtAuthentificationResponse {

    private String token;
    private String refreshToken;


}


