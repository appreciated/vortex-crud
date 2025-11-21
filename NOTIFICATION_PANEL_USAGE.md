# Notification Panel - Usage Guide

The Notification Panel is a component that displays notifications from a custom VortexCrudDataStore. It appears as a bell icon in the application header with a popover containing notification lists.

## Features

- **Data Store Integration**: Query notifications from any VortexCrudDataStore
- **Unread/All Tabs**: Separate views for unread and all notifications
- **Mark as Read**: Bulk action to mark all notifications as read
- **User Avatars**: Display user avatars and names
- **Timestamps**: Show when notifications were created
- **Filtering**: Filter notifications by user or other criteria
- **i18n Support**: Fully internationalized

## Quick Start

### 1. Create a Notification Entity (JPA Example)

```java
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @IntegerNumberField
    private Integer id;

    @TextField
    @Column(nullable = false)
    private String message;

    @DateTimePickerField
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @TextField
    private String userName;

    @TextField
    private String userAvatar;

    @CheckboxField
    @Column(nullable = false)
    private Boolean isRead = false;

    @IntegerNumberField
    private Integer targetUserId; // Optional: to filter by user

    // Constructors, getters, setters...
}
```

### 2. Create a Repository

```java
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
```

### 3. Configure the Notification Panel (JPA Example)

```java
@Service
public class MyAppConfiguration implements
    VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final NotificationRepository notificationRepository;

    @Autowired
    public MyAppConfiguration(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {

        // Configure notification panel
        NotificationPanelConfiguration<String, JpaRepository<?, ?>> notificationConfig =
            NotificationPanelConfiguration.<String, JpaRepository<?, ?>>builder()
                .dataStoreKey(notificationRepository)
                .messageField("message")
                .timestampField("createdAt")
                .userNameField("userName")
                .userAvatarField("userAvatar")
                .readStatusField("isRead")
                .readStatusValueForUnread(false)
                .limit(20)
                // Optional: filter by current user
                // .filterField("targetUserId")
                // .filterValue(getCurrentUserId())
                .build();

        // Build your routes...
        Map<String, RouteRenderer<...>> routes = ...;

        // Build application with notification panel
        return JpaApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("messages")
            .routes(routes)
            .notificationPanelConfiguration(notificationConfig)
            .build();
    }
}
```

### 4. Add i18n Messages

Add these keys to your `messages.properties` file:

```properties
# Notification Panel
notifications.heading=Notifications
notifications.unread=Unread
notifications.all=All
notifications.mark_all_read=Mark all read
notifications.no_new=No new notifications
```

## jOOQ Example

### 1. Create Database Schema

```sql
CREATE TABLE notifications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_name TEXT,
    user_avatar TEXT,
    is_read BOOLEAN NOT NULL DEFAULT 0,
    target_user_id INTEGER
);
```

### 2. Generate jOOQ Classes

```bash
mvn clean generate-sources
```

### 3. Configure the Notification Panel (jOOQ Example)

```java
@Service
public class MyAppConfiguration implements
    VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {

        // Import generated table
        import static com.example.generated.tables.Notifications.NOTIFICATIONS;

        // Configure data store
        Map<TableImpl<?>, DataStoreConfig<...>> dataStores = Map.of(
            NOTIFICATIONS, JooqDataStoreConfig.of(NOTIFICATIONS)
                .withFields(Map.of(
                    NOTIFICATIONS.ID, new IdField<>(),
                    NOTIFICATIONS.MESSAGE, new TextField<>(),
                    NOTIFICATIONS.CREATED_AT, new DateTimePickerField<>(),
                    NOTIFICATIONS.USER_NAME, new TextField<>(),
                    NOTIFICATIONS.USER_AVATAR, new TextField<>(),
                    NOTIFICATIONS.IS_READ, new CheckboxField<>(),
                    NOTIFICATIONS.TARGET_USER_ID, new IntegerNumberField<>()
                ))
                .build()
        );

        // Configure notification panel
        NotificationPanelConfiguration<TableField<?, ?>, TableImpl<?>> notificationConfig =
            NotificationPanelConfiguration.<TableField<?, ?>, TableImpl<?>>builder()
                .dataStoreKey(NOTIFICATIONS)
                .messageField(NOTIFICATIONS.MESSAGE)
                .timestampField(NOTIFICATIONS.CREATED_AT)
                .userNameField(NOTIFICATIONS.USER_NAME)
                .userAvatarField(NOTIFICATIONS.USER_AVATAR)
                .readStatusField(NOTIFICATIONS.IS_READ)
                .readStatusValueForUnread(false)
                .limit(20)
                .build();

        // Build your routes...
        Map<String, RouteRenderer<...>> routes = ...;

        // Build application with notification panel
        return JooqApplication.builder()
            .withApplicationName("application.name")
            .withI18nBundlePrefix("messages")
            .withRoutes(routes)
            .withDataStores(dataStores)
            .withNotificationPanelConfiguration(notificationConfig)
            .build();
    }
}
```

## Advanced Configuration

### Filter Notifications by Current User

To show only notifications for the logged-in user:

```java
// Get current user ID from security context
Integer currentUserId = getCurrentUserId();

NotificationPanelConfiguration.<String, JpaRepository<?, ?>>builder()
    .dataStoreKey(notificationRepository)
    .messageField("message")
    .timestampField("createdAt")
    .userNameField("userName")
    .userAvatarField("userAvatar")
    .readStatusField("isRead")
    .readStatusValueForUnread(false)
    .filterField("targetUserId")
    .filterValue(currentUserId)  // Only show notifications for this user
    .limit(20)
    .build();
```

### Customize i18n Keys

```java
NotificationPanelConfiguration.<String, JpaRepository<?, ?>>builder()
    .dataStoreKey(notificationRepository)
    .messageField("message")
    .timestampField("createdAt")
    .headingKey("custom.notifications.title")
    .unreadTabKey("custom.notifications.new")
    .allTabKey("custom.notifications.history")
    .markAllReadKey("custom.notifications.mark_read")
    .noNewNotificationsKey("custom.notifications.empty")
    .build();
```

### Set Custom Limit

```java
NotificationPanelConfiguration.<String, JpaRepository<?, ?>>builder()
    .dataStoreKey(notificationRepository)
    .messageField("message")
    .timestampField("createdAt")
    .limit(50)  // Show up to 50 notifications
    .build();
```

## Creating Notifications Programmatically

### Using VortexCrudDataStore Pattern

```java
// In your view or action handler
VortexCrudDataStore<String, Object> notificationStore =
    dataStoreRegistry.getDataStore(notificationRepository);

// Create new notification
Object notification = notificationStore.newInstance();
reflectionService.setValue(notification, "message", "Your task has been completed!");
reflectionService.setValue(notification, "createdAt", LocalDateTime.now());
reflectionService.setValue(notification, "userName", "System");
reflectionService.setValue(notification, "isRead", false);
reflectionService.setValue(notification, "targetUserId", userId);

// Save to database
notificationStore.insertRecord(notification);
```

### Using Route Actions

You can create notifications from route actions:

```java
GlobalRouteAction.<Task>builder()
    .componentFactory(() -> new Button("Notify", VaadinIcon.BELL.create()))
    .handler(context -> {
        // Get notification data store
        VortexCrudDataStore<FieldType, Object> notificationStore =
            context.dataStoreFactoryRegistry().getDataStore(notificationRepository);

        // Create notification
        Object notification = notificationStore.newInstance();
        context.reflectionService().setValue(notification, "message", "New task assigned!");
        context.reflectionService().setValue(notification, "createdAt", LocalDateTime.now());
        context.reflectionService().setValue(notification, "isRead", false);

        notificationStore.insertRecord(notification);

        context.showSuccessNotification("Notification sent!");
    })
    .build()
```

## Database Schema Requirements

Your notification table/entity must have:

- **Message field** (required): The notification content
- **Timestamp field** (required): When the notification was created
- **Read status field** (required): Boolean indicating if read
- **User name field** (optional): Name of the user who triggered the notification
- **User avatar field** (optional): URL/path to user's avatar image
- **Filter field** (optional): Field to filter notifications (e.g., target user ID)

## Troubleshooting

### Notifications Not Appearing

1. Check that the data store key is correctly configured
2. Verify that the field names match your entity/table fields
3. Ensure notifications exist in the database
4. Check browser console for JavaScript errors

### "Mark All Read" Not Working

1. Verify that `readStatusField` is configured
2. Ensure `readStatusValueForUnread` matches your unread value
3. Check that the field is writable (not read-only)

### Timestamps Not Displaying

The notification panel supports these timestamp types:
- `java.time.Instant`
- `java.time.LocalDateTime`
- `java.sql.Timestamp`
- `java.util.Date`
- `Long` (epoch milliseconds)

Ensure your timestamp field is one of these types.

## Example Screenshots

### Bell Icon in Header
The bell icon appears in the top-right header next to the logout button.

### Notification Popover
When clicked, a popover opens showing:
- Unread tab (default)
- All tab
- "Mark all read" button
- List of notifications with avatars, names, messages, and timestamps

## Complete Working Example

See the example projects for complete working implementations:
- **JPA**: `examples/jpa-sqlite-example`
- **jOOQ**: `examples/jooq-sqlite-example`

Both examples can be extended with notification functionality following this guide.
