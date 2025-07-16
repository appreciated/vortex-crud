package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqRouteRendererConfiguration extends RouteRendererConfiguration<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> {

    public JooqRouteRendererConfiguration(Class<? extends VortexCrudItemFactory<TableField<?, ?>>> factory) {
        super(factory);
    }

    public static RouteRendererConfiguration.Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of(Class<? extends VortexCrudItemFactory> factory) {
        return new RouteRendererConfiguration.Builder<>(new JooqRouteRendererConfiguration((Class<? extends VortexCrudItemFactory<TableField<?, ?>>>) factory));
    }
}

