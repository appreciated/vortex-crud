package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaFormSlideRoute extends FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static FormRoute.FormRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return FormRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .presentationMode(FormPresentationMode.SLIDE);
    }
}
