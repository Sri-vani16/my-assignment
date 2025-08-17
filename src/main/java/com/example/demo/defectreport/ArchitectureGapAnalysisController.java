package com.example.demo.defectreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/architecture-gap-report")
public class ArchitectureGapAnalysisController {
    @Autowired
    private ArchitectureGapAnalysisService architectureGapAnalysisService;

    @GetMapping
    public ArchitectureGapAnalysisReport getReport() {
        return architectureGapAnalysisService.generateReport();
    }
}
