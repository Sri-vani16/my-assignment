package com.example.demo.defectreport;

import java.util.List;
import java.util.Map;

public class ArchitectureGapAnalysisReport {
    private String executiveSummary;
    private List<String> scalabilityBottlenecks;
    private List<String> securityVulnerabilities;
    private List<String> modernizationStrategies;
    private Map<String, String> evaluationCriteria;
    private List<String> prioritizedImprovementRoadmap;

    public ArchitectureGapAnalysisReport(String executiveSummary,
                                         List<String> scalabilityBottlenecks,
                                         List<String> securityVulnerabilities,
                                         List<String> modernizationStrategies,
                                         Map<String, String> evaluationCriteria,
                                         List<String> prioritizedImprovementRoadmap) {
        this.executiveSummary = executiveSummary;
        this.scalabilityBottlenecks = scalabilityBottlenecks;
        this.securityVulnerabilities = securityVulnerabilities;
        this.modernizationStrategies = modernizationStrategies;
        this.evaluationCriteria = evaluationCriteria;
        this.prioritizedImprovementRoadmap = prioritizedImprovementRoadmap;
    }

    public String getExecutiveSummary() { return executiveSummary; }
    public List<String> getScalabilityBottlenecks() { return scalabilityBottlenecks; }
    public List<String> getSecurityVulnerabilities() { return securityVulnerabilities; }
    public List<String> getModernizationStrategies() { return modernizationStrategies; }
    public Map<String, String> getEvaluationCriteria() { return evaluationCriteria; }
    public List<String> getPrioritizedImprovementRoadmap() { return prioritizedImprovementRoadmap; }
}
