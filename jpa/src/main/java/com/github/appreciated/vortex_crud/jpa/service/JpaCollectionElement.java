package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;

public class JpaCollectionElement extends InternalFormElement<String, String> {

    public JpaCollectionElement(String label) {
        super(null, "collection", label);
    }

    public static Builder<String, String> of(String label) {
        return new Builder<>(new JpaCollectionElement(label));
    }
}


