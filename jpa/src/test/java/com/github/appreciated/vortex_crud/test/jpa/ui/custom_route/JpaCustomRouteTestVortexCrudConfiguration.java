package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.CustomRoute;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaGridRoute;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JpaCustomRouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaCustomRouteTestRepository itemRepository;

    public JpaCustomRouteTestVortexCrudConfiguration(JpaCustomRouteTestRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // Configure data stores
        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = Map.of(
                itemRepository, JpaDataStoreConfig.builder(itemRepository)
                        .fields(Map.of(
                                "id", IdField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "name", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "description", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()
                        ))
                        .build()
        );

        // Configure routes - including CustomRoute for the custom dashboard view
        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();

        // Standard VortexCrud route
        routes.put("items", JpaGridRoute.builder()
                .dataStoreKey(itemRepository)
                .title("route.items.title")
                .iconFactory(() -> VaadinIcon.LIST.create())
                .build());

        // CustomRoute - integrates the @Route annotated CustomDashboardView into the menu
        routes.put("dashboard", CustomRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .title("route.dashboard.title")
                .viewClass(CustomDashboardView.class)
                .iconFactory(() -> VaadinIcon.DASHBOARD.create())
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
