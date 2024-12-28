package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import org.jooq.Table;

public class JooqRoute extends Route<Table<?>> {
    public JooqRoute(Class<? extends TurboCrudRouteFactory> factory) {
        super(factory);
    }

    public static Route.Builder<Table<?>> of(Class<? extends TurboCrudRouteFactory> factory) {
        return new Route.Builder<Table<?>>(new Route<>(factory));
    }
}
