package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;

public class JpaFieldElement extends InternalFormElement<String, String> {

    public JpaFieldElement(String field, String label) {
        super(field, "field", label);
    }

    public static Builder<String, String> of(String field, String label) {
        return new Builder<>(new JpaFieldElement(field, label));
    }
}


