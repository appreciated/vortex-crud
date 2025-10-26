package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.ViewFieldType;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaFieldElement extends InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public JpaFieldElement(String field, String label) {
        super(field, ViewFieldType.FIELD, label);
    }

    public static Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> of(String field, String label) {
        return new Builder<>(new JpaFieldElement(field, label));
    }
}

