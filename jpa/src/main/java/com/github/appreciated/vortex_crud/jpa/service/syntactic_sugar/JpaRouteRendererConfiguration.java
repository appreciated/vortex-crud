package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class JpaRouteRendererConfiguration extends RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public static RouteRendererConfiguration.RouteRendererConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> of(Class<? extends VortexCrudItemFactory> factory) {
        return RouteRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory((Class<? extends VortexCrudItemFactory<String>>) factory);
    }
}

