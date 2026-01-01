package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCollectionElement extends InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public static Collection.CollectionBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> builder(String label) {
        return Collection.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().label(label);
    }
}
