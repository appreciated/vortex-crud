package com.github.appreciated.turbo_crud.jpa.service;

import com.github.appreciated.turbo_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;

public class JpaRouteRendererConfiguration extends RouteRendererConfiguration<String, String> {
    public JpaRouteRendererConfiguration(Class<? extends TurboCrudItemFactory<String>> factory) {
        super(factory);
    }

    public static RouteRendererConfiguration.Builder<String, String> of(Class<? extends TurboCrudItemFactory> factory) {
        return new RouteRendererConfiguration.Builder<>(new JpaRouteRendererConfiguration((Class<? extends TurboCrudItemFactory<String>>) factory));
    }
}

