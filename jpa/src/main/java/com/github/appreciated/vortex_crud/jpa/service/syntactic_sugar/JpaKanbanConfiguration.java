package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.KanbanConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaKanbanConfiguration {
    public static KanbanConfiguration.KanbanConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return KanbanConfiguration.builder();
    }
}