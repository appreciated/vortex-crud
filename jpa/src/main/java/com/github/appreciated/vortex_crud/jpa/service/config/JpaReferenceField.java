package com.github.appreciated.vortex_crud.jpa.service.config;

import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class JpaReferenceField extends ReferenceField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {


    public JpaReferenceField(JpaRepository<?, ?> dataStore, String filterField, List<String> children, boolean required, Validation validation) {
        super(dataStore, filterField, children, required, validation);
    }
}
