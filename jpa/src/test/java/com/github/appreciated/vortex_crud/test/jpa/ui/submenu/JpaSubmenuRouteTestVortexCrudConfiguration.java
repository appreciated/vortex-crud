package com.github.appreciated.vortex_crud.test.jpa.ui.submenu;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.SingleFormRoute;
import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaSingleFormRoute;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaSubmenuRoute;
import com.github.appreciated.vortex_crud.test.jpa.ui.subroute.JpaSubrouteTestRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaSubmenuRouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaSubrouteTestRepository taskRepository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaSubmenuRouteTestVortexCrudConfiguration(JpaSubrouteTestRepository taskRepository, JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.taskRepository = taskRepository;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var taskStore = new JpaRepositoryDataStore<>(taskRepository, annotationRegistryService, new DataStoreHooks<>());
        // We reuse the Task entity/repository but map "title" to "Username" for the purpose of the test
        var taskConfig = JpaDataStoreConfig.builder(taskRepository, taskStore)
                        .fields(Map.of(
                                "id", NumericIdField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "title", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()
                        ))
                        .build();

        // 1. General Settings (Single Form)
        SingleFormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> generalSettings = JpaSingleFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("General Settings")
                .titleField("title")
                .entityFilterField("id")
                .entityFilterValueProvider(() -> 1) // Assuming ID 1 exists
                .children(List.of(
                        JpaFieldElement.builder("title", "Setting Value").build()
                ))
                .build();

        // 2. Nested Submenu Routes
        // 2.1 Security (Single Form)
        SingleFormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> securitySettings = JpaSingleFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("Security")
                .titleField("title")
                .entityFilterField("id")
                .entityFilterValueProvider(() -> 1)
                .children(List.of(
                         JpaFieldElement.builder("title", "Security Level").build()
                ))
                .build();

        // 2.2 Profile (Single Form)
        SingleFormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> profileSettings = JpaSingleFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("Profile")
                .titleField("title")
                .entityFilterField("id")
                .entityFilterValueProvider(() -> 1)
                .children(List.of(
                        JpaFieldElement.builder("title", "Username").build()
                ))
                .build();

        // 3. Advanced Settings (Submenu containing Security and Profile)
        var advancedSettings = JpaSubmenuRoute.builder()
                .dataStoreConfig(taskConfig) // Dummy datastore, submenu doesn't strictly need it if children have their own
                .title("Advanced Settings")
                .childrenMap(Map.of(
                        "security", securitySettings,
                        "profile", profileSettings
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("submenu-test", JpaSubmenuRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("Submenu Test")
                .childrenMap(Map.of(
                        "general", generalSettings,
                        "advanced", advancedSettings
                ))
                .build());

        return JpaApplication.builder()
                .applicationName("Submenu Test App")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
