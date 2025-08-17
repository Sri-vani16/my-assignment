package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.AuthenticationException;
import com.example.demo.exception.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    
    private final Set<String> blacklistedTokens = new HashSet<>();

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }
        return jwtService.generateToken(user);
    }

    public void logout(String token) {
        jwtService.validateToken(token); // Ensure token is valid before blacklisting
        blacklistedTokens.add(token);
    }

    public boolean validateToken(String token) {
        if (blacklistedTokens.contains(token)) {
            return false;
        }
        try {
            jwtService.validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void requireRole(String token, User.Role requiredRole) {
        if (!validateToken(token)) {
            throw new AuthenticationException("Invalid or expired token");
        }
        
        String userRole = jwtService.getRoleFromToken(token);
        if (!User.Role.valueOf(userRole).equals(requiredRole)) {
            throw new AuthorizationException("Insufficient privileges");
        }
    }

    public void requireAdminRole(String token) {
        requireRole(token, User.Role.ADMIN);
    }

    public String getUsernameFromToken(String token) {
        if (!validateToken(token)) {
            throw new AuthenticationException("Invalid or expired token");
        }
        return jwtService.getUsernameFromToken(token);
    }
}