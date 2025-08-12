package com.petland.modules.dashboard.strategies.impl;

import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.strategies.generate.GenerateReportPDF;
import com.petland.modules.dashboard.strategies.ReportStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendReportViaPDF implements ReportStrategy<byte[]> {

    private final GenerateReportPDF reportViaPDF;

    public byte[] generate(Report report) {
        return reportViaPDF.generate(report);
    }
}
