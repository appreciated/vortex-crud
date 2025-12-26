package com.github.appreciated.vortex_crud.test.jpa.ui.single_component.single_component_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaSingleComponentRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Service
public class JpaSingleComponentRouteVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<Object, String, JpaRepository<?, ?>> {

    private final JpaSingleComponentRouteRepository repository;
    private final JpaFieldAnnotationRegistryService registry;

    public JpaSingleComponentRouteVortexCrudConfiguration(JpaSingleComponentRouteRepository repository,
                                                          JpaFieldAnnotationRegistryService registry) {
        this.repository = repository;
        this.registry = registry;
    }

    @Override
    public Application<Object, String, JpaRepository<?, ?>> get() {
        var store = new JpaRepositoryDataStore<>(repository, registry);
        var config = JpaDataStoreConfig.builder(repository, store).build();

        LinkedHashMap<String, RouteRenderer<Object, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();

        // Single Component Route
        routes.put("single-component-test", JpaSingleComponentRoute.builder()
             .dataStoreConfig(config)
             .title("Single Component")
             .entityFilterField("id")
             .entityFilterValueProvider(() -> 1L)
             .field("notes") // This is the single component field
             .build());

        return JpaApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .selects(Selects.builder().configs(new HashMap<>()).build())
            .build();
    }
}
