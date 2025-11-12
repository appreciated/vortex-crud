package com.github.appreciated.vortex_crud.demo.projectmanagement.enums;

public enum TaskStatus {
    TODO("todo"),
    IN_PROGRESS("in_progress"),
    IN_REVIEW("in_review"),
    DONE("done"),
    BLOCKED("blocked");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
