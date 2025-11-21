package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FileFieldRendererConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaFileFieldRendererConfiguration extends FileFieldRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static FileFieldRendererConfiguration.FileFieldRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return FileFieldRendererConfiguration.builder();
    }
}
