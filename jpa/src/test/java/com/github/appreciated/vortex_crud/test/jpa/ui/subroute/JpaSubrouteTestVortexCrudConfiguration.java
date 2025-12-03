package com.github.appreciated.vortex_crud.test.jpa.ui.subroute;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
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
        var taskStore = new JpaRepositoryDataStore<>(taskRepository, annotationRegistryService, new DataStoreHooks<>());
        var taskConfig = JpaDataStoreConfig.builder(taskRepository, taskStore)
                        .fields(Map.of(
                                "id", IdField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "title", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "url", ImageField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().configuration(
                                        ImageFieldRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                                .resourceProvider(new LocalImageResourceProvider())
                                                .build()
                                ).build()
                        ))
                        .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JpaSubmenuRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("route.tasks.title")
                .childrenMap(Map.of(
                        "open", JpaMasterDetailRoute.builder()
                                .dataStoreConfig(taskConfig)
                                .title("route.open-tasks.title")
                                .configuration(JpaGridItemRendererConfiguration.builder()
                                        .titleField("title")
                                        .build())
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
