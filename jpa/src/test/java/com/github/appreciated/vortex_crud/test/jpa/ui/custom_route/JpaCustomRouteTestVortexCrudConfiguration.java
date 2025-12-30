package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.CustomRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Minimal CustomRoute configuration - just pass your component class.
 */
@Service
public class JpaCustomRouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        Map<String, RouteRenderer<?, ?, ?>> routes = Map.of(
                "dashboard", CustomRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .componentClass(CustomDashboardView.class)
                        .title("route.dashboard.title")
                        .iconFactory(VaadinIcon.DASHBOARD::create)
                        .isDefaultRoute(true)
                        .build()
        );

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
