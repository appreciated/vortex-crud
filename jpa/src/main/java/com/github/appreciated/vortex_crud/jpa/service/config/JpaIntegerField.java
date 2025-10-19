package com.github.appreciated.vortex_crud.jpa.service.config;

import com.github.appreciated.vortex_crud.core.config.model.fields.IntegerField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for integer number fields.
 */
public class JpaIntegerField extends IntegerField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
