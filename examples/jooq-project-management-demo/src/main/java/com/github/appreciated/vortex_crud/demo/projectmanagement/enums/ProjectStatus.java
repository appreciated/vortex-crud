package com.github.appreciated.vortex_crud.demo.projectmanagement.enums;

public enum ProjectStatus {
    PLANNING("planning"),
    ACTIVE("active"),
    ON_HOLD("on_hold"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String value;

    ProjectStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
