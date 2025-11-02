package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridItemRendererConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaGridItemRendererConfiguration {
    public static GridItemRendererConfiguration.GridItemRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return GridItemRendererConfiguration.builder();
    }
}
