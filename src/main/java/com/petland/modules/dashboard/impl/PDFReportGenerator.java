package com.petland.modules.dashboard.impl;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.dashboard.generator.IssueReportPDF;
import com.petland.modules.dashboard.interfaces.PDFGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PDFReportGenerator implements PDFGenerator<Report> {

    private final IssueReportPDF reportsPDF;

    public byte[] generate(Report report) {
        return reportsPDF.generateReport(report);
    }
}
