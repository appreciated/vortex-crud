package com.github.appreciated.vortex_crud.demo.projectmanagement.enums;

public enum TaskType {
    TASK("task"),
    BUG("bug"),
    STORY("story"),
    EPIC("epic"),
    SUBTASK("subtask");

    private final String value;

    TaskType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
