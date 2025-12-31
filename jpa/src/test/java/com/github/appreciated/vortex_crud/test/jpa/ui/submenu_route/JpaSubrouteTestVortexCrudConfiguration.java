package com.github.appreciated.vortex_crud.test.jpa.ui.submenu_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRoute;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaMasterDetailRoute;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaSubmenuRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaSubrouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaSubrouteTestRepository taskRepository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaSubrouteTestVortexCrudConfiguration(JpaSubrouteTestRepository taskRepository, JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.taskRepository = taskRepository;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var taskStore = new JpaRepositoryDataStore<>(taskRepository, annotationRegistryService);
        var taskConfig = JpaDataStoreConfig.builder(taskRepository, taskStore)
                        .fields(Map.of(
                                "id", NumericIdField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "title", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "url", ImageField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                        .resourceProvider(new LocalImageResourceProvider())
                                        .build()
                        ))
                        .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> taskForm = JpaFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .titleField("title")
                .children(List.of(
                        JpaFieldElement.builder("title", "route.tasks.labels.title").build(),
                        JpaFieldElement.builder("url", "route.tasks.labels.image").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("tasks", JpaSubmenuRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("route.tasks.title")
                .childrenMap(Map.of(
                        "open", JpaMasterDetailRoute.builder()
                                .dataStoreConfig(taskConfig)
                                .title("route.open-tasks.title")
                                .titleField("title")
                                .form(taskForm)
                                .build()
                ))
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
