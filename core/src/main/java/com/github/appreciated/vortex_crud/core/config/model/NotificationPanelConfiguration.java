package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import lombok.experimental.Accessors;

/**
 * Configuration for a notification panel that displays notifications from a custom data store.
 * The notification panel appears as a bell icon in the application header with a popover containing notifications.
 *
 * <p>Example usage (JPA):</p>
 * <pre>{@code
 * NotificationPanelConfiguration.<String, JpaRepository<?, ?>>builder()
 *     .dataStoreKey(notificationRepository)
 *     .messageField("message")
 *     .timestampField("createdAt")
 *     .userNameField("userName")
 *     .userAvatarField("userAvatar")
 *     .readStatusField("isRead")
 *     .readStatusValueForUnread(false)
 *     .limit(20)
 *     .build()
 * }</pre>
 *
 * <p>Example usage (jOOQ):</p>
 * <pre>{@code
 * NotificationPanelConfiguration.<TableField<?, ?>, TableImpl<?>>builder()
 *     .dataStoreKey(NOTIFICATIONS)
 *     .messageField(NOTIFICATIONS.MESSAGE)
 *     .timestampField(NOTIFICATIONS.CREATED_AT)
 *     .userNameField(NOTIFICATIONS.USER_NAME)
 *     .userAvatarField(NOTIFICATIONS.USER_AVATAR)
 *     .readStatusField(NOTIFICATIONS.IS_READ)
 *     .readStatusValueForUnread(false)
 *     .limit(20)
 *     .build()
 * }</pre>
 *
 * @param <FieldType> The type used to identify fields (String for JPA, TableField for jOOQ)
 * @param <RepositoryType> The type of repository/table key (JpaRepository for JPA, TableImpl for jOOQ)
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class NotificationPanelConfiguration<FieldType, RepositoryType> {

    /**
     * The data store key (repository or table) containing notification data
     */
    private RepositoryType dataStoreKey;

    /**
     * The data store instance (optional, if dataStoreKey is not provided)
     */
    private VortexCrudDataStore<FieldType, ?> dataStore;

    public static class NotificationPanelConfigurationBuilder<FieldType, RepositoryType> {
        private VortexCrudDataStore<FieldType, ?> dataStore;

        public NotificationPanelConfigurationBuilder<FieldType, RepositoryType> dataStore(VortexCrudDataStore<FieldType, ?> dataStore) {
            this.dataStore = dataStore;
            return this;
        }
    }

    /**
     * Field containing the notification message/content
     */
    private FieldType messageField;

    /**
     * Field containing the timestamp when the notification was created
     */
    private FieldType timestampField;

    /**
     * Field containing the user name who triggered the notification (optional)
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
    private String headingKey = "notifications.heading";

    /**
     * i18n key for the "Unread" tab label
     * Default: "notifications.unread"
     */
    @Builder.Default
    private String unreadTabKey = "notifications.unread";

    /**
     * i18n key for the "All" tab label
     * Default: "notifications.all"
     */
    @Builder.Default
    private String allTabKey = "notifications.all";

    /**
     * i18n key for the "Mark all read" button
     * Default: "notifications.mark_all_read"
     */
    @Builder.Default
    private String markAllReadKey = "notifications.mark_all_read";

    /**
     * i18n key for the "No new notifications" message
     * Default: "notifications.no_new"
     */
    @Builder.Default
    private String noNewNotificationsKey = "notifications.no_new";

    /**
     * Aria label for the bell button
     * Default: "Notifications"
     */
    @Builder.Default
    private String ariaLabel = "Notifications";
}
