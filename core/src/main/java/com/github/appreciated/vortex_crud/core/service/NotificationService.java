package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService<ModelClass, FieldType, RepositoryType> {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final ReflectionService<FieldType> reflectionService;

    public NotificationService(@Lazy VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                               ReflectionService<FieldType> reflectionService) {
        this.configService = configService;
        this.reflectionService = reflectionService;
    }

    public void createNotification(String message, Object targetUserId, String actorName, String actorAvatarUrl) {
        try {
            if (configService.configuration() == null) {
                // Config not loaded yet or not available
                return;
            }

            NotificationPanelConfiguration<ModelClass, FieldType, RepositoryType> config = configService.configuration().notificationPanelConfiguration();

            if (config == null) {
                log.debug("Notification configuration is missing. Skipping notification creation.");
                return;
            }

            if (config.dataStoreConfig() == null || config.dataStoreConfig().dataStoreInstance() == null) {
                log.warn("Notification DataStore is not configured properly.");
                return;
            }

            VortexCrudDataStore<FieldType, ModelClass> dataStore = config.dataStoreConfig().dataStoreInstance();
            ModelClass notification = dataStore.newInstance();

            if (config.messageField() != null) {
                reflectionService.setValue(notification, config.messageField(), message);
            }

            if (config.timestampField() != null) {
                reflectionService.setValue(notification, config.timestampField(), LocalDateTime.now());
            }

            if (config.userNameField() != null && actorName != null) {
                reflectionService.setValue(notification, config.userNameField(), actorName);
            }

            if (config.userAvatarField() != null && actorAvatarUrl != null) {
                reflectionService.setValue(notification, config.userAvatarField(), actorAvatarUrl);
            }

            if (config.readStatusField() != null) {
                reflectionService.setValue(notification, config.readStatusField(), config.readStatusValueForUnread());
            }

            if (config.filterField() != null && targetUserId != null) {
                reflectionService.setValue(notification, config.filterField(), targetUserId);
            }

            dataStore.insertRecord(notification);
        } catch (Exception e) {
            log.error("Failed to create notification", e);
        }
    }
}
