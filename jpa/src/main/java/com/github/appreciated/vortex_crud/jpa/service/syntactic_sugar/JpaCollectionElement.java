package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.config.model.FormElement;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

@SuperBuilder
public class JpaCollectionElement extends FormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public static Collection.CollectionBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder(String label) {
        return Collection.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().label(label);
    }
}
