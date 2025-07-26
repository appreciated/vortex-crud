package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Kanban;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaKanban extends Kanban<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public JpaKanban(Class<? extends VortexCrudItemFactory<String>> factory) {
        super(factory);
    }

    public static class Builder extends Kanban.Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
        public Builder(JpaKanban product) {
            super(product);
        }
    }

    public static Builder of(Class<? extends VortexCrudItemFactory> factory) {
        return new Builder(new JpaKanban((Class<? extends VortexCrudItemFactory<String>>) factory));
    }
}