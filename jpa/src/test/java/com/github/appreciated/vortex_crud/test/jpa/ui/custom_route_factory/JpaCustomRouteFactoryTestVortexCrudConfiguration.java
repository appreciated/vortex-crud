package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route_factory;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.CustomRoute;
import com.github.appreciated.vortex_crud.core.config.model.SubmenuRoute;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class JpaCustomRouteFactoryTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        return Application.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .applicationName("JPA Custom Route Factory Test")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(new LinkedHashMap<>(Map.of(
                        "submenu", SubmenuRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .title("Submenu")
                                .childrenMap(new LinkedHashMap<>(Map.of(
                                        "custom-nested", CustomRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                                .title("Nested Custom")
                                                // No component class provided, or even if provided, nested route handled by InternalDynamicRoute uses factory
                                                .build()
                                )))
                                .build()
                )))
                .build();
    }
}
