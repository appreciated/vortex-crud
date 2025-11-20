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
 * Simplified VortexCrud configuration for CustomRoute testing.
 * <p>
 * This demonstrates how to add a CustomRoute entry to the VortexCrud menu.
 * No entities, repositories, or data stores needed - just a link to your custom @Route view.
 * </p>
 */
@Service
public class JpaCustomRouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // Simply add a CustomRoute that links to the @Route annotated CustomDashboardView
        Map<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = Map.of(
                "dashboard", CustomRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .title("route.dashboard.title")
                        .iconFactory(() -> VaadinIcon.DASHBOARD.create())
                        .isDefaultRoute(true)  // This will be the landing page
                        .build()
        );

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
