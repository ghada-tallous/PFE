package com.example.Pfe.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;


}

