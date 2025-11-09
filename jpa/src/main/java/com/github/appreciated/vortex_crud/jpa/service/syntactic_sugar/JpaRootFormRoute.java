package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RootFormRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaRootFormRoute extends RootFormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static RootFormRoute.RootFormRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return RootFormRoute.rootFormRouteBuilder();
    }
}
