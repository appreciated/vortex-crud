package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.RouteConfiguration;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqRouteConfiguration extends RouteConfiguration<Table<?>, TableField<?,?>> {
    public JooqRouteConfiguration(Class<? extends TurboCrudItemFactory<TableField<?,?>>> factory) {
        super(factory);
    }

    public static RouteConfiguration.Builder<Table<?>, TableField<?,?>> of(Class<? extends TurboCrudItemFactory> factory) {
        return new RouteConfiguration.Builder<>(new JooqRouteConfiguration((Class<? extends TurboCrudItemFactory<TableField<?, ?>>>) factory));
    }
}

