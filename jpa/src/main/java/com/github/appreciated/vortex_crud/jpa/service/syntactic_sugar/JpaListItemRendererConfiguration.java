package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.ListItemRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaListItemRendererConfiguration {
    public static ListItemRendererConfiguration.ListItemRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> builder() {
        return ListItemRendererConfiguration.builder();
    }

    public static ListItemRendererConfiguration.ListItemRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> of(Class<? extends VortexCrudItemFactory> factory) {
        return ListItemRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory((Class<? extends VortexCrudItemFactory<String>>) factory);
    }
}