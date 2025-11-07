package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.ListItemRendererConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaListItemRendererConfiguration extends ListItemRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static ListItemRendererConfiguration.ListItemRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return ListItemRendererConfiguration.builder();
    }
}