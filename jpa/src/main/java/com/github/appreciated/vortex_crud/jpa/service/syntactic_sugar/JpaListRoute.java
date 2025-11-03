package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaListRoute {
    public static ListRoute.ListRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder();
    }
}