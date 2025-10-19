package com.github.appreciated.vortex_crud.jpa.service.config;

import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for select fields.
 */
public class JpaSelectField extends SelectField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public JpaSelectField(String values) {
        super(values);
    }
}
