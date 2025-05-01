package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.springframework.data.repository.CrudRepository;

public class JpaCollectionElement extends InternalFormElement<CrudRepository<?,?>, String> {

    public JpaCollectionElement(String label) {
        super(null, "collection", label);
    }

    public static Builder<CrudRepository<?,?>, String> of(String label) {
        return new Builder<>(new JpaCollectionElement(label));
    }
}


