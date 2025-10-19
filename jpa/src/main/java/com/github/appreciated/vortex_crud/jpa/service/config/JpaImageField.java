package com.github.appreciated.vortex_crud.jpa.service.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for image fields.
 */
public class JpaImageField extends ImageField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public JpaImageField(RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configuration) {
        super(configuration);
    }
}
