package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridOrListRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class JpaGridOrListRendererConfiguration extends GridOrListRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public static GridOrListRendererConfiguration.GridOrListRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> of(Class<? extends VortexCrudItemFactory> factory) {
        return GridOrListRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory((Class<? extends VortexCrudItemFactory<String>>) factory);
    }
}
