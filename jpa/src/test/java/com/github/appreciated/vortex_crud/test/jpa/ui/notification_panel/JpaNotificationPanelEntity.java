package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import com.github.appreciated.vortex_crud.jpa.service.annoations.CheckboxField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.DateTimePickerField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.IntegerNumberField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
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
    @IntegerNumberField
    private Integer id;
    @TextField
    private String message;
    @DateTimePickerField
    private LocalDateTime timestamp;
    @CheckboxField
    private boolean read;
}
