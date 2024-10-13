package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

public class ValidationConfig {
    @Optional
    private int maxLength;

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
