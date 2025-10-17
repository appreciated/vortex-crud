package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for text area fields.
 */
public class JpaTextAreaField extends TextAreaField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
