package com.example.demo.defectreport;

import java.util.List;
import java.util.Map;

public class DefectAnalysisReport {
    private String executiveSummary;
    private Map<String, Integer> severityDistribution;
    private double averageResolutionTime;
    private List<String> rootCauses;
    private List<String> recurringIssues;
    private List<String> recommendations;

    public DefectAnalysisReport(String executiveSummary,
                                Map<String, Integer> severityDistribution,
                                double averageResolutionTime,
                                List<String> rootCauses,
                                List<String> recurringIssues,
                                List<String> recommendations) {
        this.executiveSummary = executiveSummary;
        this.severityDistribution = severityDistribution;
        this.averageResolutionTime = averageResolutionTime;
        this.rootCauses = rootCauses;
        this.recurringIssues = recurringIssues;
        this.recommendations = recommendations;
    }

    public String getExecutiveSummary() { return executiveSummary; }
    public Map<String, Integer> getSeverityDistribution() { return severityDistribution; }
    public double getAverageResolutionTime() { return averageResolutionTime; }
    public List<String> getRootCauses() { return rootCauses; }
    public List<String> getRecurringIssues() { return recurringIssues; }
    public List<String> getRecommendations() { return recommendations; }
}
