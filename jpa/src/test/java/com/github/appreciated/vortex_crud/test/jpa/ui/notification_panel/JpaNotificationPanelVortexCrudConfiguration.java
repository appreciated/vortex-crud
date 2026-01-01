package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaGridRoute;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

@Configuration
public class JpaNotificationPanelVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaNotificationPanelRepository repository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;
    private final JpaFieldService fieldService;

    public JpaNotificationPanelVortexCrudConfiguration(JpaNotificationPanelRepository repository,
                                                        JpaFieldAnnotationRegistryService annotationRegistryService,
                                                        JpaFieldService fieldService) {
        this.repository = repository;
        this.annotationRegistryService = annotationRegistryService;
        this.fieldService = fieldService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var dataStore = new JpaRepositoryDataStore<>(repository, annotationRegistryService);
        DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>> config = JpaDataStoreConfig.builder(repository, dataStore)
                .withServices(fieldService, Map.of(JpaNotificationPanelEntity.class, dataStore))
                .fields(Map.of())
                .build();

        // Add a simple grid route so the router layout renders
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> gridRoute = JpaGridRoute.builder()
                .dataStoreConfig(config)
                .title("Home")
                .titleField("message")
                .defaultRoute(true)
                .build();

        return JpaApplication.builder()
                .applicationName("Notification Test App")
                .i18nBundlePrefix("ui_test_i18n")
                .notificationPanelConfiguration(NotificationPanelConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .dataStoreConfig(config)
                        .messageField("message")
                        .timestampField("timestamp")
                        .readStatusField("read")
                        .build())
                .routes(Map.of("home", gridRoute))
                .build();
    }
}
