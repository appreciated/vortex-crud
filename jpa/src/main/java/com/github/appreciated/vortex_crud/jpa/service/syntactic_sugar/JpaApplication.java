package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaApplication {
    public static Application.ApplicationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> builder() {
        return Application.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder();
    }
}
