package com.example.demo.controller;

import com.example.demo.config.FeatureFlagConfig;
import com.example.demo.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feature-flags")
public class FeatureFlagController {
    
    @Autowired
    private FeatureFlagConfig featureFlagConfig;
    
    @GetMapping
    public ResponseEntity<ApiResponse<FeatureFlagConfig>> getFeatureFlags() {
        return ResponseEntity.ok(ApiResponse.success(featureFlagConfig, "Feature flags retrieved"));
    }
    
    @PostMapping("/toggle/{flag}")
    public ResponseEntity<ApiResponse<String>> toggleFlag(@PathVariable String flag, @RequestParam boolean enabled) {
        switch (flag) {
            case "new-payment-api":
                featureFlagConfig.setEnableNewPaymentApi(enabled);
                break;
            case "migration-percentage":
                // This would need a different endpoint for percentage
                break;
            default:
                return ResponseEntity.badRequest().body(ApiResponse.error("Unknown flag: " + flag, "INVALID_FLAG"));
        }
        return ResponseEntity.ok(ApiResponse.success("Flag updated", "Flag " + flag + " set to " + enabled));
    }
}