package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqRouteRenderer extends RouteRenderer<Class<? extends TableRecord<?>>, TableField<?, ?>> {
    public JooqRouteRenderer(Class<? extends VortexCrudRouteFactory<Class<? extends TableRecord<?>>, TableField<?, ?>>> factory) {
        super(factory);
    }

    public static RouteRenderer.Builder<Class<? extends TableRecord<?>>, TableField<?, ?>> of(Class<? extends VortexCrudRouteFactory> factory) {
        return new RouteRenderer.Builder<>(new JooqRouteRenderer((Class<? extends VortexCrudRouteFactory<Class<? extends TableRecord<?>>, TableField<?, ?>>>) factory));
    }
}
