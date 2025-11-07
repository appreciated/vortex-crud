package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaGridRoute extends GridRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static GridRoute.GridRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return GridRoute.builder();
    }
}