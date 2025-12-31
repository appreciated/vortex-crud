package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_panel_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaNotificationPanelEntity {
    @Id
    private Integer id;
    private String message;
    private LocalDateTime timestamp;
    private boolean read;
}
