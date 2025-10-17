package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.DateTimePickerField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for date-time picker fields.
 */
public class JpaDateTimePickerField extends DateTimePickerField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
