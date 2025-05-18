package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaRouteRenderer extends RouteRenderer<JpaRepository<?, ?>, String> {
    public JpaRouteRenderer(Class<? extends VortexCrudRouteFactory<JpaRepository<?, ?>, String>> factory) {
        super(factory);
    }

    public static RouteRenderer.Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudRouteFactory> factory) {
        return new RouteRenderer.Builder<>(new RouteRenderer<>((Class<? extends VortexCrudRouteFactory<JpaRepository<?, ?>, String>>) factory));
    }
}