package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaRouteRenderer extends RouteRenderer<JpaRepository<?, ?>, String> {
    public JpaRouteRenderer(Class<? extends VortexCrudRouteFactory<JpaRepository<?, ?>, String>> factory) {
        super(factory);
    }

    public static class Builder<DataStoreId, FieldId> extends RouteRenderer.Builder<DataStoreId, FieldId> {
        public Builder(RouteRenderer<DataStoreId, FieldId> product) {
            super(product);
        }
    }

    public static JpaRouteRenderer.Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudRouteFactory> factory) {
        return new JpaRouteRenderer.Builder<>(new JpaRouteRenderer((Class<? extends VortexCrudRouteFactory<JpaRepository<?, ?>, String>>) factory));
    }
}