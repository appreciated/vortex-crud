package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaRouteRendererConfiguration extends RouteRendererConfiguration<JpaRepository<?, ?>, String> {

    public JpaRouteRendererConfiguration(Class<? extends VortexCrudItemFactory<String>> factory) {
        super(factory);
    }

    public static RouteRendererConfiguration.Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudItemFactory> factory) {
        return new RouteRendererConfiguration.Builder<>(new JpaRouteRendererConfiguration((Class<? extends VortexCrudItemFactory<String>>) factory));
    }
}

