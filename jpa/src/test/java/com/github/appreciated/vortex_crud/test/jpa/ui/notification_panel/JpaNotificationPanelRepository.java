package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaNotificationPanelRepository extends JpaRepository<JpaNotificationPanelEntity, Integer> {
}
