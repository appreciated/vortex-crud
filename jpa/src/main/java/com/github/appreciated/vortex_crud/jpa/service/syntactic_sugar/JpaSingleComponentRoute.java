package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.SingleComponentRoute;
import com.github.appreciated.vortex_crud.core.ui.factories.route.single_component.SingleComponentRouteFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaSingleComponentRoute extends SingleComponentRoute<Object, String, JpaRepository<?, ?>> {
    public static SingleComponentRouteBuilder<Object, String, JpaRepository<?, ?>> builder() {
        return SingleComponentRoute.<Object, String, JpaRepository<?, ?>>builder()
                .factory(new SingleComponentRouteFactory<>());
    }
}
