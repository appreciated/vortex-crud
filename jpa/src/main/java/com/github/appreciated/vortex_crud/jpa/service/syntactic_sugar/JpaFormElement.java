package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.ViewFieldType;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaFormElement {
    public static InternalFormElement.InternalFormElementBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> builder(String field, String label) {
        return InternalFormElement.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .field(field)
                .label(label)
                .type(ViewFieldType.FIELD);
    }
}

