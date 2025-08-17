package com.example.demo.defectreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/defect-report")
public class DefectAnalysisController {
    @Autowired
    private DefectAnalysisService defectAnalysisService;

    @GetMapping
    public DefectAnalysisReport getReport() {
        return defectAnalysisService.generateReport();
    }
}
