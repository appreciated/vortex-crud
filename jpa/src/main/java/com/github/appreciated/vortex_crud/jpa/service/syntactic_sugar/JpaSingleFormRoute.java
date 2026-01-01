package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.SingleFormRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaSingleFormRoute extends SingleFormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static SingleFormRoute.SingleFormRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return SingleFormRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder();
    }
}
