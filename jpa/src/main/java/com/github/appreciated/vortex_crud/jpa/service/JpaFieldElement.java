package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.springframework.data.repository.CrudRepository;

public class JpaFieldElement extends InternalFormElement<CrudRepository<?,?>, String> {

    public JpaFieldElement(String field, String label) {
        super(field, "field", label);
    }

    public static Builder<CrudRepository<?,?>, String> of(String field, String label) {
        return new Builder<>(new JpaFieldElement(field, label));
    }
}


