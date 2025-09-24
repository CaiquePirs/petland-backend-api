package com.petland.modules.dashboard.report;

import com.petland.modules.dashboard.model.Report;

public interface ReportFileGenerator {
    byte[] generate(Report report);
}
