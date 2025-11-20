package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.PdfFieldRendererConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaPdfFieldRendererConfiguration extends PdfFieldRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static PdfFieldRendererConfiguration.PdfFieldRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return PdfFieldRendererConfiguration.builder();
    }
}
