package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormElement;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaFormElement {
    public static FormElement.FormElementBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> builder(String field, String label) {
        return FormElement.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .field(field)
                .label(label);
    }
}

