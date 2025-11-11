package com.github.appreciated.vortex_crud.demo.devplatform.enums;

public enum IssueState {
    OPEN("open"),
    CLOSED("closed");

    private final String value;

    IssueState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
