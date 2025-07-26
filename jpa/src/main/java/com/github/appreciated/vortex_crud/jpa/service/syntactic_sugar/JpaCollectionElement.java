package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.springframework.data.jpa.repository.JpaRepository;


public class JpaCollectionElement extends InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public JpaCollectionElement(String label) {
        super(null, "collection", label);
    }

    public static Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> of(String label) {
        return new Builder<>(new JpaCollectionElement(label));
    }
}


