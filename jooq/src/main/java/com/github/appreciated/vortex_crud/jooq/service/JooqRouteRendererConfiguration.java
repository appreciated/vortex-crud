package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqRouteRendererConfiguration extends RouteRendererConfiguration<Table<?>, TableField<?,?>> {

    public JooqRouteRendererConfiguration(Class<? extends VortexCrudItemFactory<TableField<?, ?>>> factory) {
        super(factory);
    }

    public static RouteRendererConfiguration.Builder<Table<?>, TableField<?,?>> of(Class<? extends VortexCrudItemFactory> factory) {
        return new RouteRendererConfiguration.Builder<>(new JooqRouteRendererConfiguration((Class<? extends VortexCrudItemFactory<TableField<?, ?>>>) factory));
    }
}

