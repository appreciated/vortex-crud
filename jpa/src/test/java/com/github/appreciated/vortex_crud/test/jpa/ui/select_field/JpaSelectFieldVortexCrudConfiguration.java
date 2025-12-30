package com.github.appreciated.vortex_crud.test.jpa.ui.select_field;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaSelectFieldVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaSelectFieldRepository selectFieldRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaSelectFieldVortexCrudConfiguration(
            JpaSelectFieldRepository selectFieldRepository,
            JpaFieldService fieldService,
            JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.selectFieldRepository = selectFieldRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // Create the data store
        var store = new JpaRepositoryDataStore<>(selectFieldRepository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudQueryDataStore> storeMap = Map.of(store.getModelClass(), store);

        var config = JpaDataStoreConfig.builder(selectFieldRepository, store)
                .withServices(fieldService, storeMap)
                .build();

        // Define select options
        LinkedHashMap<String, String> options = new LinkedHashMap<>();
        options.put("Option 1", "Option 1 Label");
        options.put("Option 2", "Option 2 Label");

        // Form Route
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> form = JpaFormRoute.builder()
                .dataStoreConfig(config)
                .title("Select Field Test")
                .titleField("name")
                .children(List.of(
                        JpaFieldElement.builder("name", "Name Select").build()
                ))
                .build();

        // List Route
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("select-field-test", ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreConfig(config)
                .title("Select Field List")
                .filterField("name")
                .children(List.of(
                        JpaFieldElement.builder("name", "Name").build()
                ))
                .form(form)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .selects(Selects.builder()
                        .configs(Map.of("name-options", options))
                        .build())
                .build();
    }
}
