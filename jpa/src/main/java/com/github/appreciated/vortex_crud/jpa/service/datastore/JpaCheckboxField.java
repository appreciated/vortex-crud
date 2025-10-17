package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.CheckboxField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for checkbox fields.
 */
public class JpaCheckboxField extends CheckboxField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
