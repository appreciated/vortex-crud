package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.RouteRenderer;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqRouteRenderer extends RouteRenderer<Table<?>, TableField<?, ?>> {
    public JooqRouteRenderer(Class<? extends TurboCrudRouteFactory<Table<?>, TableField<?, ?>>> factory) {
        super(factory);
    }

    public static RouteRenderer.Builder<Table<?>, TableField<?, ?>> of(Class<? extends TurboCrudRouteFactory> factory) {
        return new RouteRenderer.Builder<>(new JooqRouteRenderer((Class<? extends TurboCrudRouteFactory<Table<?>, TableField<?, ?>>>) factory));
    }
}
