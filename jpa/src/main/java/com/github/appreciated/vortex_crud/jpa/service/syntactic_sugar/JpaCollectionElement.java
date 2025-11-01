package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.ViewFieldType;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCollectionElement {
    public static InternalFormElement.InternalFormElementBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> of(String label) {
        return InternalFormElement.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .label(label)
                .type(ViewFieldType.COLLECTION);
    }
}

