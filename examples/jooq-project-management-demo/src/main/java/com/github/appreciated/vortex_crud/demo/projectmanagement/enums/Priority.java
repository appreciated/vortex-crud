package com.github.appreciated.vortex_crud.demo.projectmanagement.enums;

public enum Priority {
    LOWEST("lowest"),
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    HIGHEST("highest"),
    CRITICAL("critical");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
