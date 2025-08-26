package com.petland.modules.dashboard.strategy;

import com.petland.modules.dashboard.report.Report;

public interface ReportStrategy<T> {
    T generate(Report report);
}
