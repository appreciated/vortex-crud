package com.github.appreciated.vortex_crud.test.jpa.ui.missing_features;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.GlobalRouteAction;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.vaadin.flow.component.button.Button;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.COG;
import static com.vaadin.flow.component.icon.VaadinIcon.PRINT;

@Service
public class JpaMissingFeaturesVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaMissingFeaturesRepository repository;
    private JpaFieldAnnotationRegistryService annotationRegistryService;
    private final JpaFieldService fieldService;

    public JpaMissingFeaturesVortexCrudConfiguration(JpaMissingFeaturesRepository repository, JpaFieldAnnotationRegistryService annotationRegistryService, JpaFieldService fieldService) {
        this.repository = repository;
        this.annotationRegistryService = annotationRegistryService;
        this.fieldService = fieldService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // Selects config with explicit typing
        LinkedHashMap<String, String> tagOptions = new LinkedHashMap<>();
        tagOptions.put("tag1", "Tag 1");
        tagOptions.put("tag2", "Tag 2");

        Map<String, LinkedHashMap<?, String>> selectsConfig = new HashMap<>();
        selectsConfig.put("tags", tagOptions);

        Selects selects = Selects.builder()
            .configs(selectsConfig)
            .build();


        var taskStore = new JpaRepositoryDataStore<>(repository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(taskStore.getModelClass(), taskStore);

        var taskConfig = JpaDataStoreConfig.builder(repository, taskStore)
                .withServices(fieldService, storeMap)
                .build();

        // Form Route
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> form = JpaFormRoute.builder()
            .dataStoreConfig(taskConfig)
            .title("route.missing.title")
            .formConfiguration(JpaFormRendererConfiguration.builder()
                .titleField("name")
                .children(List.of(
                    JpaFieldElement.builder("name", "Name").build(),
                    JpaFieldElement.builder("tags", "Tags").build(),
                    JpaFieldElement.builder("pdfDoc", "PDF").build(),
                    JpaFieldElement.builder("notes", "Notes").build()
                ))
                .build())
            .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();

        // List Route with Global Action
        routes.put("missing-features-test", JpaListRoute.builder()
            .dataStoreConfig(taskConfig)
            .iconFactory(COG::create)
            .title("route.missing.list")
            .configuration(JpaListItemRendererConfiguration.builder()
                 .filterField("name")
                 .children(List.of(
                      JpaFieldElement.builder("name", "Name").build()
                 ))
                 .build())
            .routeActions(List.of(
                 GlobalRouteAction.<String, JpaRepository<?, ?>>builder()
                    .componentFactory(() -> new Button("Print", PRINT.create()))
                    .handler(ctx -> {})
                    .build()
            ))
            .child(form)
            .build());

        // Single Form Route
        routes.put("single-form-test", JpaSingleFormRoute.builder()
             .dataStoreConfig(taskConfig)
             .title("Single Form")
             .entityFilterField("id")
             .entityFilterValueProvider(() -> 1L)
             .formConfiguration(JpaFormRendererConfiguration.builder()
                 .titleField("name")
                 .children(List.of(
                     JpaFieldElement.builder("name", "Name").build(),
                     JpaFieldElement.builder("tags", "Tags").build(),
                     JpaFieldElement.builder("pdfDoc", "PDF").build()
                 ))
                 .build())
             .build());

        return JpaApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .selects(selects)
            .build();
    }
}
