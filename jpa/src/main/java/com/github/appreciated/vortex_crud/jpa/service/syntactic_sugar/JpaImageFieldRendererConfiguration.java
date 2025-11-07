package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaImageFieldRendererConfiguration {
    public static ImageFieldRendererConfiguration.ImageFieldRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return ImageFieldRendererConfiguration.builder();
    }
}