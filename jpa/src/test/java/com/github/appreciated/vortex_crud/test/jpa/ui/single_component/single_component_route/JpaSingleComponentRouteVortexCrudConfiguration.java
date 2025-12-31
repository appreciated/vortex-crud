package com.github.appreciated.vortex_crud.test.jpa.ui.single_component.single_component_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaSingleComponentRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JpaSingleComponentRouteVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaSingleComponentRouteRepository repository;
    private final JpaFieldAnnotationRegistryService registry;
    private final JpaFieldService fieldService;

    public JpaSingleComponentRouteVortexCrudConfiguration(JpaSingleComponentRouteRepository repository,
                                                          JpaFieldAnnotationRegistryService registry,
                                                          JpaFieldService fieldService) {
        this.repository = repository;
        this.registry = registry;
        this.fieldService = fieldService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var store = new JpaRepositoryDataStore<>(repository, registry);
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(store.getModelClass(), store);

        var config = JpaDataStoreConfig.builder(repository, store)
                .withServices(fieldService, storeMap)
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        // Single Component Route
        routes.put("single-component-test", JpaSingleComponentRoute.builder()
             .dataStoreConfig(config)
             .title("route.single-component.title")
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
