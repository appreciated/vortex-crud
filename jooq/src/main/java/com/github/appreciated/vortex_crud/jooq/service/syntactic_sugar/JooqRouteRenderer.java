package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqRouteRenderer extends RouteRenderer<Class<? extends TableRecord<?>>, TableField<?, ?>> {
    public JooqRouteRenderer(Class<? extends VortexCrudRouteFactory<Class<? extends TableRecord<?>>, TableField<?, ?>>> factory) {
        super(factory);
    }

    public static class Builder<DataStoreId, FieldId, ModelClass>  extends RouteRenderer.Builder<DataStoreId, FieldId, ModelClass>  {
        public Builder(RouteRenderer<DataStoreId, FieldId, ModelClass>  product) {
            super(product);
        }
    }

    public static JooqRouteRenderer.Builder<Class<? extends TableRecord<?>>, TableField<?, ?>> of(Class<? extends VortexCrudRouteFactory> factory) {
        return new JooqRouteRenderer.Builder<>(new JooqRouteRenderer((Class<? extends VortexCrudRouteFactory<Class<? extends TableRecord<?>>, TableField<?, ?>>>) factory));
    }
}