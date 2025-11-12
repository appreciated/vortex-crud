package com.github.appreciated.vortex_crud.demo.devplatform.enums;

public enum PullRequestState {
    OPEN("open"),
    MERGED("merged"),
    CLOSED("closed");

    private final String value;

    PullRequestState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
