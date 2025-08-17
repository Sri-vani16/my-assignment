package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.version")
public class ApiVersionConfig {
    
    private String current = "v2";
    private String legacy = "v1";
    private String deprecationDate = "2024-12-31";
    
    public String getCurrent() { return current; }
    public void setCurrent(String current) { this.current = current; }
    
    public String getLegacy() { return legacy; }
    public void setLegacy(String legacy) { this.legacy = legacy; }
    
    public String getDeprecationDate() { return deprecationDate; }
    public void setDeprecationDate(String deprecationDate) { this.deprecationDate = deprecationDate; }
}