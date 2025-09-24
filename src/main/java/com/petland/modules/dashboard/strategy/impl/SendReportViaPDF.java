package com.petland.modules.dashboard.strategy.impl;

import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.strategy.generate.GenerateReportPDF;
import com.petland.modules.dashboard.strategy.ReportStrategy;
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
