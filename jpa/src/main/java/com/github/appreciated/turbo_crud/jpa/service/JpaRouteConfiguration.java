package com.github.appreciated.turbo_crud.jpa.service;

import com.github.appreciated.turbo_crud.core.config.model.RouteConfiguration;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;

public class JpaRouteConfiguration extends RouteConfiguration<String> {
    public JpaRouteConfiguration(Class<? extends TurboCrudItemFactory> factory) {
        super(factory);
    }

    public static RouteConfiguration.Builder<String> of(Class<? extends TurboCrudItemFactory> factory) {
        return new RouteConfiguration.Builder<>(new JpaRouteConfiguration(factory));
    }
}

