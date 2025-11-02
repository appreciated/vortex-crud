package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaRouteRendererConfiguration {
    public static RouteRendererConfiguration.RouteRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return RouteRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder();
    }
}

