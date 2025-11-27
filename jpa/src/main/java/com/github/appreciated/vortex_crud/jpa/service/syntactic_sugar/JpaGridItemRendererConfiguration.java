package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridItemRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaGridItemRendererConfiguration extends GridItemRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    @SuppressWarnings("unchecked")
    public static GridItemRendererConfiguration.GridItemRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return GridItemRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().factory((Class<? extends VortexCrudItemFactory<String>>) (Class<?>) CardFactory.class);
    }
}
