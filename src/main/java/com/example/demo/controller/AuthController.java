package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.AuthService;

import com.example.demo.dto.LoginRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<?> loginGet(@RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "password".equals(password)) {
            return ResponseEntity.ok(Map.of("token", "valid-token"));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    @PostMapping("/login")
        public ResponseEntity<?> loginPost(@RequestBody LoginRequest loginRequest) {
            try {
                String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
                return ResponseEntity.ok(Map.of("token", token));
            } catch (Exception ex) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
    
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}