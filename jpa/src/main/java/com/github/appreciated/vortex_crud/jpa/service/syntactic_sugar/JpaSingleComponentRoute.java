package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.SingleComponentRoute;
import com.github.appreciated.vortex_crud.core.ui.factories.route.single_component.SingleComponentRouteFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaSingleComponentRoute extends SingleComponentRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static SingleComponentRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return SingleComponentRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory(new SingleComponentRouteFactory<>());
    }
}
