package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCollection extends Collection<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static Collection.CollectionBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder(Class<? extends VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> factoryClass) {
        try {
            return Collection.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().factoryInstance(factoryClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate factory", e);
        }
    }
}
