package com.example.Pfe.controller;

import com.example.Pfe.dto.JwtAuthentificationResponse;
import com.example.Pfe.dto.RefreshTokenRequest;
import com.example.Pfe.dto.SignInRequest;
import com.example.Pfe.dto.SignUpRequest;
import com.example.Pfe.entites.User;
import com.example.Pfe.service.AuthenticationService;
import com.example.Pfe.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthentificationController {

    private final AuthenticationService authenticationService;
    private final UserServiceImpl userService; // Injecte UserServiceImpl
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthentificationResponse> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthentificationResponse> signin(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthentificationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }


    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        boolean confirmed = userService.confirmEmail(token);
        if (confirmed) {
            return ResponseEntity.ok("Email confirmed successfully. You can now sign in.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired confirmation token.");
        }
    }
}
