package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.springframework.data.repository.CrudRepository;

public class JpaRouteRenderer extends RouteRenderer<CrudRepository<?,?>, String> {
    public JpaRouteRenderer(Class<? extends VortexCrudRouteFactory<CrudRepository<?,?>, String>> factory) {
        super(factory);
    }

    public static RouteRenderer.Builder<CrudRepository<?,?>, String> of(Class<? extends VortexCrudRouteFactory> factory) {
        return new RouteRenderer.Builder<>(new RouteRenderer<>((Class<? extends VortexCrudRouteFactory<CrudRepository<?,?>, String>>) factory));
    }
}