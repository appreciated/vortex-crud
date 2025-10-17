package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for text fields.
 */
public class JpaTextField extends TextField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
