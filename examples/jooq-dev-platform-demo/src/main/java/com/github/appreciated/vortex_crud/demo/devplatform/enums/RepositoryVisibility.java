package com.github.appreciated.vortex_crud.demo.devplatform.enums;

public enum RepositoryVisibility {
    PUBLIC("public"),
    PRIVATE("private"),
    INTERNAL("internal");

    private final String value;

    RepositoryVisibility(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
