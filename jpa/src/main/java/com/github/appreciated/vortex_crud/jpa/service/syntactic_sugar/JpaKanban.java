package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Kanban;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaKanban {
    public static Kanban.KanbanBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> builder() {
        return Kanban.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder();
    }

    public static Kanban.KanbanBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> of(Class<? extends VortexCrudItemFactory> factory) {
        return Kanban.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory((Class<? extends VortexCrudItemFactory<String>>) factory);
    }
}