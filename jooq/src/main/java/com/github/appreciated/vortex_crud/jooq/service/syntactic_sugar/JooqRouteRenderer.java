package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqRouteRenderer extends RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public JooqRouteRenderer(Class<? extends VortexCrudRouteFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> factory) {
        super(factory);
    }

    public static class Builder<DataStoreId, FieldId, KeyType> extends RouteRenderer.Builder<DataStoreId, FieldId, KeyType> {
        public Builder(RouteRenderer<DataStoreId, FieldId, KeyType> product) {
            super(product);
        }
    }

    public static JooqRouteRenderer.Builder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(Class<? extends VortexCrudRouteFactory> factory) {
        return new JooqRouteRenderer.Builder<>(new JooqRouteRenderer((Class<? extends VortexCrudRouteFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) factory));
    }
}