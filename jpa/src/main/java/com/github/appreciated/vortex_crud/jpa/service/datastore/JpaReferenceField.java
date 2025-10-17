package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA-typed thin field for reference fields.
 */
public class JpaReferenceField extends ReferenceField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public JpaReferenceField(JpaRepository<?, ?> dataStore, String filterField, List<String> children) {
        super(dataStore, filterField, children);
    }
}
