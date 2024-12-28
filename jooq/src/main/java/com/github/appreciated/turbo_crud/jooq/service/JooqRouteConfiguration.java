package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.RouteConfiguration;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import org.jooq.Table;

public class JooqRouteConfiguration extends RouteConfiguration<Table<?>> {
    public JooqRouteConfiguration(Class<? extends TurboCrudItemFactory> factory) {
        super(factory);
    }

    public static RouteConfiguration.Builder<Table<?>> of(Class<? extends TurboCrudItemFactory> factory) {
        return new RouteConfiguration.Builder<>(new JooqRouteConfiguration(factory));
    }
}

