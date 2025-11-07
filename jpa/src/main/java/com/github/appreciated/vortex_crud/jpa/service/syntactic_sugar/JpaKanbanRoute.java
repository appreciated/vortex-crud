package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.KanbanRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaKanbanRoute extends KanbanRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static KanbanRoute.KanbanRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return KanbanRoute.builder();
    }
}