package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Configuration for a notification panel that displays notifications from a custom data store.
 * The notification panel appears as a bell icon in the application header with a popover containing notifications.
 * ...
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class NotificationPanelConfiguration<ModelClass, FieldType, RepositoryType> implements ValidatableConfiguration {

    @lombok.NonNull
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @lombok.NonNull
    private FieldType messageField;

    @lombok.NonNull
    private FieldType timestampField;

    private FieldType userNameField;

    private FieldType userAvatarField;

    @lombok.NonNull
    private FieldType readStatusField;

    @Builder.Default
    private Object readStatusValueForUnread = false;

    @Builder.Default
    private Object readStatusValueForRead = true;

    @Builder.Default
    private int limit = 20;

    private FieldType filterField;

    private Object filterValue;

    @Builder.Default
    @I18nKey
    private String headingKey = "notifications.heading";

    @Builder.Default
    @I18nKey
    private String unreadTabKey = "notifications.unread";

    @Builder.Default
    @I18nKey
    private String allTabKey = "notifications.all";

    @Builder.Default
    @I18nKey
    private String markAllReadKey = "notifications.mark_all_read";

    @Builder.Default
    @I18nKey
    private String noNewNotificationsKey = "notifications.no_new";

    @Builder.Default
    @I18nKey
    private String ariaLabel = "Notifications";
}
