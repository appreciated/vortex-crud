package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.MasterDetailRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaMasterDetailRoute {
    public static MasterDetailRoute.MasterDetailRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return MasterDetailRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder();
    }
}