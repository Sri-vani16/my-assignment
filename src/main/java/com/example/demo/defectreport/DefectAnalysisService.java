package com.example.demo.defectreport;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefectAnalysisService {
    public DefectAnalysisReport generateReport() {
        // Sample data for demonstration
        Map<String, Integer> severityDistribution = new HashMap<>();
        severityDistribution.put("Critical", 3);
        severityDistribution.put("High", 7);
        severityDistribution.put("Medium", 12);
        severityDistribution.put("Low", 5);

        double avgResolutionTime = 2.5; // days
        List<String> rootCauses = Arrays.asList("Null pointer exception", "Incorrect validation", "Database connection timeout");
        List<String> recurringIssues = Arrays.asList("Validation errors in login", "Timeouts in payment API");
        List<String> recommendations = Arrays.asList(
            "Implement stricter input validation",
            "Improve database connection pooling",
            "Add automated regression tests for login and payment modules"
        );

        String executiveSummary = "This report analyzes recent defects, highlights recurring issues, and provides actionable recommendations to improve code quality and reduce resolution time.";

        return new DefectAnalysisReport(
            executiveSummary,
            severityDistribution,
            avgResolutionTime,
            rootCauses,
            recurringIssues,
            recommendations
        );
    }
}
