package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "feature.flags")
public class FeatureFlagConfig {
    
    private boolean useNewPaymentApi = false;
    private boolean enableNewValidation = false;
    private boolean useUpdatedDatabase = false;
    private boolean enableNewErrorHandling = true;
    private double migrationPercentage = 0.0;
    private boolean enableLegacySupport = true;
    private boolean enableNewPaymentApi = false;
    private boolean enableNewAuthSystem = false;
    private boolean enableNewUserManagement = false;
    private boolean enableNewCaching = false;
    
    public boolean isUseNewPaymentApi() { return useNewPaymentApi; }
    public void setUseNewPaymentApi(boolean useNewPaymentApi) { this.useNewPaymentApi = useNewPaymentApi; }
    
    public boolean isEnableNewValidation() { return enableNewValidation; }
    public void setEnableNewValidation(boolean enableNewValidation) { this.enableNewValidation = enableNewValidation; }
    
    public boolean isUseUpdatedDatabase() { return useUpdatedDatabase; }
    public void setUseUpdatedDatabase(boolean useUpdatedDatabase) { this.useUpdatedDatabase = useUpdatedDatabase; }
    
    public boolean isEnableNewErrorHandling() { return enableNewErrorHandling; }
    public void setEnableNewErrorHandling(boolean enableNewErrorHandling) { this.enableNewErrorHandling = enableNewErrorHandling; }
    
    public double getMigrationPercentage() { return migrationPercentage; }
    public void setMigrationPercentage(double migrationPercentage) { this.migrationPercentage = migrationPercentage; }
    
    public boolean isEnableLegacySupport() { return enableLegacySupport; }
    public void setEnableLegacySupport(boolean enableLegacySupport) { this.enableLegacySupport = enableLegacySupport; }
    
    public boolean isEnableNewPaymentApi() { return enableNewPaymentApi; }
    public void setEnableNewPaymentApi(boolean enableNewPaymentApi) { this.enableNewPaymentApi = enableNewPaymentApi; }
    
    public boolean isEnableNewAuthSystem() { return enableNewAuthSystem; }
    public void setEnableNewAuthSystem(boolean enableNewAuthSystem) { this.enableNewAuthSystem = enableNewAuthSystem; }
    
    public boolean isEnableNewUserManagement() { return enableNewUserManagement; }
    public void setEnableNewUserManagement(boolean enableNewUserManagement) { this.enableNewUserManagement = enableNewUserManagement; }
    
    public boolean isEnableNewCaching() { return enableNewCaching; }
    public void setEnableNewCaching(boolean enableNewCaching) { this.enableNewCaching = enableNewCaching; }
}