package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
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
        DataStoreConfig config = JpaDataStoreConfig.builder(repository, dataStore)
                .withServices(fieldService, Map.of(JpaNotificationPanelEntity.class, dataStore))
                .build();
        
        return JpaApplication.builder()
                .applicationName("Notification Test App")
                .notificationPanelConfiguration(NotificationPanelConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .dataStoreConfig(config)
                        .messageField("message")
                        .timestampField("timestamp")
                        .readStatusField("read")
                        .build())
                .routes(Collections.emptyMap())
                .build();
    }
}
