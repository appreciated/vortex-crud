package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaGridRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JpaNotificationPanelVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaNotificationRepository repository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;
    private final JpaFieldService fieldService;

    public JpaNotificationPanelVortexCrudConfiguration(JpaNotificationRepository repository, JpaFieldAnnotationRegistryService annotationRegistryService, JpaFieldService fieldService) {
        this.repository = repository;
        this.annotationRegistryService = annotationRegistryService;
        this.fieldService = fieldService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var notificationStore = new JpaRepositoryDataStore<>(repository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(notificationStore.getModelClass(), notificationStore);

        var notificationConfig = JpaDataStoreConfig.builder(repository, notificationStore)
                .withServices(fieldService, storeMap)
                .build();

        NotificationPanelConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> notificationPanelConfig =
                NotificationPanelConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .dataStoreConfig(notificationConfig)
                        .messageField("message")
                        .timestampField("timestamp")
                        .readStatusField("read")
                        .readStatusValueForUnread(false)
                        .readStatusValueForRead(true)
                        .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("notifications", JpaGridRoute.builder()
                .dataStoreConfig(notificationConfig)
                .title("Notifications")
                .isDefaultRoute(true)
                .titleField("message")
                .build());

        return JpaApplication.builder()
                .applicationName("Notification Test App")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .notificationPanelConfiguration(notificationPanelConfig)
                .build();
    }
}
