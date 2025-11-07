package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormRendererConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaFormRendererConfiguration extends FormRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static FormRendererConfiguration.FormRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return FormRendererConfiguration.builder();
    }
}
