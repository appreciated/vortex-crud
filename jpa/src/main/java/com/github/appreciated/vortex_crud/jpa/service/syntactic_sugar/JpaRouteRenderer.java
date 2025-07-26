package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaRouteRenderer extends RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public JpaRouteRenderer(Class<? extends VortexCrudRouteFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> factory) {
        super(factory);
    }

    public static class Builder<DataStoreId, FieldId, KeyType> extends RouteRenderer.Builder<DataStoreId, FieldId, KeyType> {
        public Builder(RouteRenderer<DataStoreId, FieldId, KeyType> product) {
            super(product);
        }
    }

    public static JpaRouteRenderer.Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> of(Class<? extends VortexCrudRouteFactory> factory) {
        return new JpaRouteRenderer.Builder<>(new JpaRouteRenderer((Class<? extends VortexCrudRouteFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) factory));
    }
}