package com.github.appreciated.vortex_crud.test.jpa.ui.kanban;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaKanbanTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaKanbanTestRepository taskRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaKanbanTestVortexCrudConfiguration(JpaKanbanTestRepository taskRepository, JpaFieldService fieldService, JpaFieldAnnotationRegistryService annotationRegistryService) {
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
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(JpaFieldElement.builder("title", "route.tasks.labels.title").build()))
                        .build())
                .build();

        LinkedHashMap<String, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put("a", "enums.option1");
        enumOptions.put("b", "enums.option2");
        enumOptions.put("c", "enums.option3");

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JpaKanbanRoute.builder()
                .iconFactory(VaadinIcon.TASKS::create)
                .dataStoreConfig(taskConfig)
                .title("route.open-tasks.title")
                .configuration(JpaKanbanConfiguration.builder()
                        .titleField("title")
                        .descriptionField("description")
                        .columnField("status")
                        .rowIndexField("rowIndex")
                        .filterField("title")
                        .build())
                .child(taskForm)
                .build()
        );

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .selects(Selects.builder().configs(Map.of("enum-options", enumOptions)).build())
                .build();
    }
}
