package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.IntegerNumberField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for integer number fields.
 */
public class JpaIntegerNumberField extends IntegerNumberField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
