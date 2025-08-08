package com.petland.modules.dashboard.interfaces;


public interface PPDFGenerator<T> {
    byte[] generate(T report);
}
