package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel.JpaNotificationEntity;

public interface JpaNotificationRepository extends JpaRepository<JpaNotificationEntity, Long> {
}
