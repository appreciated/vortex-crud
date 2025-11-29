package com.github.appreciated.vortex_crud.test.jpa.ui.master_detail;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.CHECK_CIRCLE;

@Service
public class JpaMasterDetailTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaMasterDetailTestRepository taskRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaMasterDetailTestVortexCrudConfiguration(JpaMasterDetailTestRepository taskRepository, JpaFieldService fieldService, JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.taskRepository = taskRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var taskStore = new JpaRepositoryDataStore<>(taskRepository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(taskStore.getModelClass(), taskStore);

        var taskConfig = JpaDataStoreConfig.builder(taskRepository, taskStore)
                .withServices(fieldService, storeMap)
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> taskForm = JpaFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("route.projects.title-cards")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.builder("title", "route.tasks.labels.title").build(),
                                JpaFieldElement.builder("description", "route.tasks.labels.description").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JpaMasterDetailRoute.builder()
                .iconFactory(CHECK_CIRCLE::create)
                .dataStoreConfig(taskConfig)
                .title("route.done-tasks.title")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .descriptionField("status")
                        .build())
                .child(taskForm)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
