package com.petland.modules.dashboard.reports.pdf;

public interface PDFGenerator<T> {
    byte[] generate(T report);
}
