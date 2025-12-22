package com.github.appreciated.vortex_crud.test.jpa.ui.id_field;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRoute;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaListRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaIdFieldTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaIdFieldRepository repository;
    private JpaFieldAnnotationRegistryService annotationRegistryService;
    private final JpaFieldService fieldService;

    public JpaIdFieldTestVortexCrudConfiguration(JpaIdFieldRepository repository, JpaFieldAnnotationRegistryService annotationRegistryService, JpaFieldService fieldService) {
        this.repository = repository;
        this.annotationRegistryService = annotationRegistryService;
        this.fieldService = fieldService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var store = new JpaRepositoryDataStore<>(repository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(store.getModelClass(), store);

        var config = JpaDataStoreConfig.builder(repository, store)
                .withServices(fieldService, storeMap)
                .build();

        // Form Route
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> form = JpaFormRoute.builder()
            .dataStoreConfig(config)
            .title("Form")
            .titleField("name")
            .children(List.of(
                JpaFieldElement.builder("id", "ID").build(),
                JpaFieldElement.builder("name", "Name").build()
            ))
            .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();

        // List Route
        routes.put("id-test-list", JpaListRoute.builder()
            .dataStoreConfig(config)
            .title("List")
            .filterField("name")
            .children(List.of(
                  JpaFieldElement.builder("name", "Name").build()
            ))
            .child(form)
            .build());

        return JpaApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .build();
    }
}
