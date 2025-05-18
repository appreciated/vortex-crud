package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCollectionElement extends InternalFormElement<JpaRepository<?, ?>, String> {

    public JpaCollectionElement(String label) {
        super(null, "collection", label);
    }

    public static Builder<JpaRepository<?, ?>, String> of(String label) {
        return new Builder<>(new JpaCollectionElement(label));
    }
}


