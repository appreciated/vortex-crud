package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.fields.VideoField;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-typed thin field for video fields.
 */
public class JpaVideoField extends VideoField<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public JpaVideoField(RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configuration) {
        super(configuration);
    }
}
