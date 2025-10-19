package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.DoubleField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for double number fields.
 */
public class JpaDoubleNumberField extends DoubleField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
