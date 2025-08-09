package com.petland.modules.dashboard.interfaces;

public interface PDFGenerator<T> {
    byte[] generate(T report);
}
