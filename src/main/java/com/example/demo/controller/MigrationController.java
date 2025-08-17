package com.example.demo.controller;

import com.example.demo.config.FeatureFlagConfig;
import com.example.demo.dto.*;
import com.example.demo.service.MigrationAwarePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/migration")
public class MigrationController {
    
    @Autowired
    private FeatureFlagConfig featureFlags;
    
    @Autowired
    private MigrationAwarePaymentService migrationService;
    
    @GetMapping("/flags")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> getFeatureFlags() {
        Map<String, Boolean> flags = new HashMap<>();
        flags.put("useNewPaymentApi", featureFlags.isUseNewPaymentApi());
        flags.put("enableNewValidation", featureFlags.isEnableNewValidation());
        flags.put("useUpdatedDatabase", featureFlags.isUseUpdatedDatabase());
        flags.put("enableNewErrorHandling", featureFlags.isEnableNewErrorHandling());
        
        return ResponseEntity.ok(ApiResponse.success(flags, "Feature flags retrieved"));
    }
    
    @PostMapping("/toggle/{flag}")
    public ResponseEntity<ApiResponse<String>> toggleFlag(@PathVariable String flag, @RequestParam boolean enabled) {
        switch (flag) {
            case "payment-api":
                featureFlags.setUseNewPaymentApi(enabled);
                break;
            case "validation":
                featureFlags.setEnableNewValidation(enabled);
                break;
            case "database":
                featureFlags.setUseUpdatedDatabase(enabled);
                break;
            case "error-handling":
                featureFlags.setEnableNewErrorHandling(enabled);
                break;
            default:
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Unknown feature flag: " + flag, "INVALID_FLAG"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Flag updated", "Feature flag " + flag + " set to " + enabled));
    }
    
    @PostMapping("/test-payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> testMigrationPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = migrationService.processPayment(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Migration test completed"));
    }
}