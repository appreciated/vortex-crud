package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.DateField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for date fields.
 */
public class JpaDateField extends DateField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
