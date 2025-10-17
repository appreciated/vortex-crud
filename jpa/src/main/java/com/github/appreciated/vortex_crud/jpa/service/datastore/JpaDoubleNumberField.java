package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.DoubleNumberField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for double number fields.
 */
public class JpaDoubleNumberField extends DoubleNumberField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
