package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqRouteRenderer extends RouteRenderer<Table<?>, TableField<?, ?>> {
    public JooqRouteRenderer(Class<? extends VortexCrudRouteFactory<Table<?>, TableField<?, ?>>> factory) {
        super(factory);
    }

    public static RouteRenderer.Builder<Table<?>, TableField<?, ?>> of(Class<? extends VortexCrudRouteFactory> factory) {
        return new RouteRenderer.Builder<>(new JooqRouteRenderer((Class<? extends VortexCrudRouteFactory<Table<?>, TableField<?, ?>>>) factory));
    }
}
