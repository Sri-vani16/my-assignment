package com.example.demo.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.User;
import com.example.demo.entity.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.exception.TokenValidationException;
import com.example.demo.dto.TokenResponse;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long accessTokenExpiration = 900000; // 15 minutes (OAuth 2.0 recommended)
    private final long refreshTokenExpiration = 604800000; // 7 days
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public TokenResponse generateTokens(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new TokenResponse(accessToken, accessTokenExpiration / 1000, refreshToken);
    }

    private String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .claim("token_type", "access_token")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key)
                .compact();
    }

    @Transactional
    private String generateRefreshToken(User user) {
        refreshTokenRepository.revokeAllByUser(user);
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    @Transactional
    public TokenResponse refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(refreshTokenValue)
                .orElseThrow(() -> new TokenValidationException("Invalid refresh token"));
        
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new TokenValidationException("Refresh token expired");
        }
        
        return generateTokens(refreshToken.getUser());
    }

    @Transactional
    public void revokeRefreshToken(String refreshTokenValue) {
        refreshTokenRepository.findByTokenAndRevokedFalse(refreshTokenValue)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new TokenValidationException("Invalid or expired token");
        }
    }

    public String getUsernameFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return validateToken(token).get("role", String.class);
    }

    // Legacy method for backward compatibility
    @Deprecated
    public String generateToken(User user) {
        return generateAccessToken(user);
    }
}