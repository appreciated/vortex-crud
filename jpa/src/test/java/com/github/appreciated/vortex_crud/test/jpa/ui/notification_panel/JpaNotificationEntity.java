package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class JpaNotificationEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private LocalDateTime timestamp;
    private Boolean read;
}
