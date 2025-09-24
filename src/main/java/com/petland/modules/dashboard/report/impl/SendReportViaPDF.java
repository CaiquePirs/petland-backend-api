package com.petland.modules.dashboard.report.impl;

import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.report.ReportFileGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendReportViaPDF {

    private final ReportFileGenerator reportFileGenerator;

    public byte[] generate(Report report) {
        return reportFileGenerator.generate(report);
    }
}
