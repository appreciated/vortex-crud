package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for id fields.
 */
public class JpaIdField extends IdField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
