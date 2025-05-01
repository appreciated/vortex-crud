package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.springframework.data.repository.CrudRepository;

public class JpaRouteRendererConfiguration extends RouteRendererConfiguration<CrudRepository<?,?>, String> {
    public JpaRouteRendererConfiguration(Class<? extends VortexCrudItemFactory<String>> factory) {
        super(factory);
    }

    public static RouteRendererConfiguration.Builder<CrudRepository<?,?>, String> of(Class<? extends VortexCrudItemFactory> factory) {
        return new RouteRendererConfiguration.Builder<>(new JpaRouteRendererConfiguration((Class<? extends VortexCrudItemFactory<String>>) factory));
    }
}

