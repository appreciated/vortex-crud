package com.github.appreciated.vortex_crud.test.jpa.ui.search_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.SearchRoute;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaGridRoute;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

@Configuration
public class JpaSearchRouteVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaSearchRouteRepository repository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;
    private final JpaFieldService fieldService;

    public JpaSearchRouteVortexCrudConfiguration(
            JpaSearchRouteRepository repository,
            JpaFieldAnnotationRegistryService annotationRegistryService,
            JpaFieldService fieldService) {
        this.repository = repository;
        this.annotationRegistryService = annotationRegistryService;
        this.fieldService = fieldService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // Create DataStore and Config for the search route test entity
        var dataStore = new JpaRepositoryDataStore<>(repository, annotationRegistryService);
        var config = JpaDataStoreConfig.builder(repository, dataStore)
                .withServices(fieldService, Map.of(JpaSearchRouteEntity.class, dataStore))
                .build();

        // Create the grid route that will be searchable
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> gridRoute = JpaGridRoute.builder()
                .dataStoreConfig(config)
                .title("Grid")
                .filterField("name")
                .build();

        // Create search route with explicit searchable routes
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> searchRoute = SearchRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .title("Search")
                .defaultRoute(true)
                .searchableRoutes(List.of(gridRoute))
                .build();

        return JpaApplication.builder()
                .applicationName("Search Test App")
                .routes(Map.of(
                        "grid", gridRoute,
                        "search", searchRoute
                ))
                .build();
    }
}
