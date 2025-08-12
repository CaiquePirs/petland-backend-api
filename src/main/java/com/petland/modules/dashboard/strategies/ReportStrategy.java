package com.petland.modules.dashboard.strategies;

import com.petland.modules.dashboard.model.Report;

public interface ReportStrategy<T> {
    T generate(Report report);
}
