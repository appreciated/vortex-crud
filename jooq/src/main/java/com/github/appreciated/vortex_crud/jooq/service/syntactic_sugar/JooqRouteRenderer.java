package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqRouteRenderer extends RouteRenderer<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> {
    public JooqRouteRenderer(Class<? extends VortexCrudRouteFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>> factory) {
        super(factory);
    }

    public static class Builder<DataStoreId, FieldId, KeyType> extends RouteRenderer.Builder<DataStoreId, FieldId, KeyType> {
        public Builder(RouteRenderer<DataStoreId, FieldId, KeyType> product) {
            super(product);
        }
    }

    public static JooqRouteRenderer.Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of(Class<? extends VortexCrudRouteFactory> factory) {
        return new JooqRouteRenderer.Builder<>(new JooqRouteRenderer((Class<? extends VortexCrudRouteFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory));
    }
}