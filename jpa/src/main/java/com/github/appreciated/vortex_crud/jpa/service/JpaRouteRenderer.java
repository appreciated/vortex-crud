package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;

public class JpaRouteRenderer extends RouteRenderer<String, String> {
        public JpaRouteRenderer(Class<? extends VortexCrudRouteFactory<String, String>> factory) {
            super(factory);
        }

        public static RouteRenderer.Builder<String, String> of(Class<? extends VortexCrudRouteFactory> factory) {
            return new RouteRenderer.Builder<>(new RouteRenderer<>((Class<? extends VortexCrudRouteFactory<String, String>>)factory));
        }
    }