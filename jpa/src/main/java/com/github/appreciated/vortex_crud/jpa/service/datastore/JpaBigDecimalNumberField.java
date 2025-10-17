package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.BigDecimalNumberField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for BigDecimal numbers.
 */
public class JpaBigDecimalNumberField extends BigDecimalNumberField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}
