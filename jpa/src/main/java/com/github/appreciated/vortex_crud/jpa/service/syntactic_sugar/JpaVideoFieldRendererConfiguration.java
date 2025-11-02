package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.VideoFieldRendererConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaVideoFieldRendererConfiguration {
    public static VideoFieldRendererConfiguration.VideoFieldRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return VideoFieldRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder();
    }
}