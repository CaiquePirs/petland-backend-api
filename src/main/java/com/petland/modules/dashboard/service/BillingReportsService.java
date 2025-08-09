package com.petland.modules.dashboard.service;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.dashboard.reports.pdf.IssueReportPDF;
import com.petland.modules.dashboard.reports.BillingReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BillingReportsService {

    private final IssueReportPDF issueReportPDF;
    private final BillingReport billingReport;

    public byte[] issueBillingReportByPDF(LocalDate dateMin, LocalDate dateMax){
        Report report = billingReport.generate(dateMin, dateMax);
        return issueReportPDF.generateReport(report);
    }
}
