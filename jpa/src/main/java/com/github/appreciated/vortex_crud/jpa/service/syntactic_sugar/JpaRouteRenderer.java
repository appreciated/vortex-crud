package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaRouteRenderer {
    public static ListRoute.ListRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> of(Class<? extends VortexCrudRouteFactory> factory) {
        return ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory((Class<? extends VortexCrudRouteFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) factory);
    }
}