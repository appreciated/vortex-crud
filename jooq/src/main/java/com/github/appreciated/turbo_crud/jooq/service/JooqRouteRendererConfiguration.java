package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqRouteRendererConfiguration extends RouteRendererConfiguration<Table<?>, TableField<?,?>> {

    public JooqRouteRendererConfiguration(Class<? extends TurboCrudItemFactory<TableField<?, ?>>> factory) {
        super(factory);
    }

    public static RouteRendererConfiguration.Builder<Table<?>, TableField<?,?>> of(Class<? extends TurboCrudItemFactory> factory) {
        return new RouteRendererConfiguration.Builder<>(new JooqRouteRendererConfiguration((Class<? extends TurboCrudItemFactory<TableField<?, ?>>>) factory));
    }
}

