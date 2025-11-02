package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.SubmenuRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaSubmenuRoute {
    public static SubmenuRoute.SubmenuRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return SubmenuRoute.builder();
    }
}