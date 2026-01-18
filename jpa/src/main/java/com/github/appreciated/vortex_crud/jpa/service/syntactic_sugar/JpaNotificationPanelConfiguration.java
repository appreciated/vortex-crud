package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaNotificationPanelConfiguration extends NotificationPanelConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static NotificationPanelConfiguration.NotificationPanelConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return NotificationPanelConfiguration.builder();
    }
}
