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
public class NotificationPanelConfiguration<ModelClass, FieldType, RepositoryType> implements I18nKeyCollector {

    /**
     * The data store config containing notification data
     */
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    /**
     * Field containing the notification message/content
     */
    private FieldType messageField;

    /**
     * Field containing the timestamp when the notification was created
     */
    private FieldType timestampField;

    /**
     * Field containing the username who triggered the notification (optional)
     */
    private FieldType userNameField;

    /**
     * Field containing the URL/path to the user's avatar image (optional)
     */
    private FieldType userAvatarField;

    /**
     * Field indicating whether the notification has been read
     */
    private FieldType readStatusField;

    /**
     * The value that indicates a notification is unread (typically false)
     * Default: false
     */
    @Builder.Default
    private Object readStatusValueForUnread = false;

    /**
     * The value that indicates a notification is read (typically true)
     * Default: true
     */
    @Builder.Default
    private Object readStatusValueForRead = true;

    /**
     * Maximum number of notifications to display in each tab (unread/all)
     * Default: 20
     */
    @Builder.Default
    private int limit = 20;

    /**
     * Field to filter notifications by (optional, e.g., target user ID)
     * If provided, only notifications matching the filterValue will be shown
     */
    private FieldType filterField;

    /**
     * Value to filter notifications by (optional, e.g., current user's ID)
     * This is typically set dynamically based on the logged-in user
     */
    private Object filterValue;

    /**
     * i18n key for the "Notifications" heading
     * Default: "notifications.heading"
     */
    @Builder.Default
    @I18nKey
    private String headingKey = "notifications.heading";

    /**
     * i18n key for the "Unread" tab label
     * Default: "notifications.unread"
     */
    @Builder.Default
    @I18nKey
    private String unreadTabKey = "notifications.unread";

    /**
     * i18n key for the "All" tab label
     * Default: "notifications.all"
     */
    @Builder.Default
    @I18nKey
    private String allTabKey = "notifications.all";

    /**
     * i18n key for the "Mark all read" button
     * Default: "notifications.mark_all_read"
     */
    @Builder.Default
    @I18nKey
    private String markAllReadKey = "notifications.mark_all_read";

    /**
     * i18n key for the "No new notifications" message
     * Default: "notifications.no_new"
     */
    @Builder.Default
    @I18nKey
    private String noNewNotificationsKey = "notifications.no_new";

    /**
     * Aria label for the bell button
     * Default: "Notifications"
     */
    @Builder.Default
    @I18nKey
    private String ariaLabel = "Notifications";
}
