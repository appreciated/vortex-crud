package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormSlideRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaFormSlideRoute extends FormSlideRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static FormSlideRoute.FormSlideRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return FormSlideRoute.builder();
    }
}