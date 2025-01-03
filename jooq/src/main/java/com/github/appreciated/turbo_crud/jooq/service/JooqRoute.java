package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqRoute extends Route<Table<?>, TableField<?, ?>> {
    public JooqRoute(Class<? extends TurboCrudRouteFactory<Table<?>, TableField<?, ?>>> factory) {
        super(factory);
    }

    public static Route.Builder<Table<?>, TableField<?, ?>> of(Class<? extends TurboCrudRouteFactory> factory) {
        return new Route.Builder<>(new JooqRoute((Class<? extends TurboCrudRouteFactory<Table<?>, TableField<?, ?>>>) factory));
    }
}
