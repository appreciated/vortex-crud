package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.CustomRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaGridRoute;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * VortexCrud configuration for CustomRoute testing.
 * <p>
 * This configuration demonstrates:
 * <ul>
 *   <li>How to add a CustomRoute entry to the VortexCrud menu</li>
 *   <li>How to mix CustomRoute with standard VortexCrud routes</li>
 *   <li>Proper configuration of title and icon for custom routes</li>
 * </ul>
 * </p>
 */
@Service
public class JpaCustomRouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaCustomRouteTestRepository repository;

    public JpaCustomRouteTestVortexCrudConfiguration(JpaCustomRouteTestRepository repository) {
        this.repository = repository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // Define routes - mix of custom and standard VortexCrud routes
        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();

        // CustomRoute - links to the @Route annotated CustomDashboardView
        routes.put("dashboard", CustomRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .title("route.dashboard.title")
                .iconFactory(() -> VaadinIcon.DASHBOARD.create())
                .isDefaultRoute(false)
                .build());

        // Standard VortexCrud GridRoute for comparison
        routes.put("items", JpaGridRoute.builder()
                .dataStoreKey(repository)
                .title("route.items.title")
                .defaultRoute(true)  // This will be the landing page
                .configuration(com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaGridItemRendererConfiguration.builder()
                        .titleField("name")
                        .descriptionField("description")
                        .build())
                .child(com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRoute.builder()
                        .dataStoreKey(repository)
                        .title("route.items.form.title")
                        .formConfiguration(JpaFormRendererConfiguration.builder()
                                .titleField("name")
                                .children(List.of(
                                        JpaFieldElement.builder("name", "field.name").build(),
                                        JpaFieldElement.builder("description", "field.description").build()
                                ))
                                .build())
                        .build())
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
