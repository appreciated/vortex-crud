package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Versioning;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaVersioning {
    public static Versioning.VersioningBuilder<JpaRepository<?, ?>> of() {
        return Versioning.<JpaRepository<?, ?>>builder();
    }
}