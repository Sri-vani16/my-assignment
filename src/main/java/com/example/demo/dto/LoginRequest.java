package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class LoginRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username contains invalid characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", 
             message = "Password must contain uppercase, lowercase, digit and special character")
    private String password;

    @Pattern(regexp = "^(password|refresh_token)$", message = "Invalid grant type")
    private String grantType = "password";

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username != null ? username.trim() : null; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getGrantType() { return grantType; }
    public void setGrantType(String grantType) { this.grantType = grantType; }
}

