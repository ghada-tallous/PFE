package com.example.Pfe.dto;


public class PasswordResetConfirmRequest {
    private String newPassword;
    private String token;

    // Getters and setters
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
