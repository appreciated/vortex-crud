package com.github.appreciated.vortex_crud.demo.projectmanagement.enums;

public enum SprintStatus {
    PLANNED("planned"),
    ACTIVE("active"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String value;

    SprintStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
