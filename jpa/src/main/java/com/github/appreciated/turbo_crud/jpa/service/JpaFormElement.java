package com.github.appreciated.turbo_crud.jpa.service;

import com.github.appreciated.turbo_crud.core.config.model.InternalFormElement;

public class JpaFormElement extends InternalFormElement<String, String> {
    public JpaFormElement() {
    }

    public JpaFormElement(String field, String type, String label) {
        super(field, type, label);
    }

    public static Builder<String, String> of(String field, String type, String label) {
        return new Builder<>(new InternalFormElement<>(field, type, label));
    }
}

