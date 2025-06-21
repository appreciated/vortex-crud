package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaApplication extends Application<JpaRepository<?, ?>, String> {

    public static Application.Builder<JpaRepository<?, ?>, String> of() {
        return new Application.Builder<>(new JpaApplication());
    }
}
