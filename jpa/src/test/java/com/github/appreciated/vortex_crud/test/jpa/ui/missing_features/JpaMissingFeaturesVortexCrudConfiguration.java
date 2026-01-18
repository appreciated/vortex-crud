package com.github.appreciated.vortex_crud.test.jpa.ui.missing_features;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.GlobalRouteAction;
import com.github.appreciated.vortex_crud.core.ui.actions.MultiEntityRouteAction;
import com.github.appreciated.vortex_crud.core.ui.actions.SingleEntityRouteAction;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class JpaMissingFeaturesVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaMissingFeaturesRepository repository;
    private final JpaMissingFeaturesReferencedRepository referencedRepository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;
    private final JpaFieldService fieldService;

    public JpaMissingFeaturesVortexCrudConfiguration(JpaMissingFeaturesRepository repository, JpaMissingFeaturesReferencedRepository referencedRepository, JpaFieldAnnotationRegistryService annotationRegistryService, JpaFieldService fieldService) {
        this.repository = repository;
        this.referencedRepository = referencedRepository;
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

        var taskStore = new JpaRepositoryDataStore<>(repository, annotationRegistryService);
        var referencedStore = new JpaRepositoryDataStore<>(referencedRepository, annotationRegistryService);

        Map<Class<?>, VortexCrudDataStore> storeMap = new HashMap<>();
        storeMap.put(taskStore.getModelClass(), taskStore);
        storeMap.put(referencedStore.getModelClass(), referencedStore);

        var taskConfig = JpaDataStoreConfig.builder(repository, taskStore)
                .withServices(fieldService, storeMap)
                .build();

        var referencedConfig = JpaDataStoreConfig.builder(referencedRepository, referencedStore)
                .withServices(fieldService, storeMap)
                .build();

        // Form Route
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> form = JpaFormRoute.builder()
            .titleField("name")
            .fields(List.of(
                JpaFormElement.builder("name", "Name").build(),
                JpaFormElement.builder("tags", "Tags").build(),
                JpaFormElement.builder("pdfDoc", "PDF").build(),
                JpaFormElement.builder("notes", "Notes").build(),
                JpaFormElement.builder("referencedEntity", "Referenced").build(),
                JpaFormElement.builder("multiSelectEntities", "Multi Select").build(),
                JpaFormElement.builder("markdownContent", "Markdown").build(),
                JpaFormElement.builder("fileAttachment", "File").build(),
                JpaFormElement.builder("price", "Price").build(),
                JpaFormElement.builder("videoUrl", "Video").build()
            ))
            .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        // List Route with Global, Single, Multi and Menu Actions
        routes.put("missing-features-test-new", JpaListRoute.builder()
            .dataStoreConfig(taskConfig)
            .iconFactory(COG::create)
            .title("route.missing.list")
            .searchField("name")
            .columns(List.of(
                  JpaFormElement.builder("name", "Name").build()
            ))
            .actions(List.of(
                 GlobalRouteAction.<String, JpaRepository<?, ?>>builder()
                    .componentFactory(() -> new Button("Print", PRINT.create()))
                    .handler(ctx -> Notification.show("Global Action Executed"))
                    .build(),
                 SingleEntityRouteAction.<String, JpaRepository<?, ?>>builder()
                    .componentFactory(() -> new Button("Single", PENCIL.create()))
                    .handler(ctx -> Notification.show("Single Action Executed"))
                    .build(),
                 MultiEntityRouteAction.<String, JpaRepository<?, ?>>builder()
                    .componentFactory(() -> new Button("Multi", TRASH.create()))
                    .handler(ctx -> Notification.show("Multi Action Executed"))
                    .build()
            ))
            .form(form)
            .build());

        // Single Form Route
        routes.put("single-form-test", JpaSingleFormRoute.builder()
             .dataStoreConfig(taskConfig)
             .title("Single Form")
             .entityFilterField("id")
             .entityFilterValueProvider(() -> 1L)
             .titleField("name")
             .fields(List.of(
                 JpaFormElement.builder("name", "Name").build()
             ))
             .build());

        return JpaApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .selects(selects)
            .build();
    }
}
