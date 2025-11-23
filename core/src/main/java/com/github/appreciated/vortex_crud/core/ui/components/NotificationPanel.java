package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * A notification panel component that displays notifications from a custom VortexCrudDataStore.
 * Renders as a bell icon button with a popover containing notification lists.
 *
 * <p>The panel displays two tabs:</p>
 * <ul>
 *   <li><b>Unread</b> - Shows only unread notifications</li>
 *   <li><b>All</b> - Shows all notifications</li>
 * </ul>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Queries notifications from a configured VortexCrudDataStore</li>
 *   <li>Supports filtering by user or other criteria</li>
 *   <li>Displays user avatars and timestamps</li>
 *   <li>Mark all notifications as read functionality</li>
 *   <li>Fully internationalized with i18n keys</li>
 * </ul>
 *
 * @param <ModelClass> The type of model/entity class
 * @param <FieldType> The type used to identify fields (String for JPA, TableField for jOOQ)
 * @param <RepositoryType> The type of repository/table key
 */
public class NotificationPanel<ModelClass, FieldType, RepositoryType> extends Div {

    private static final Logger log = LoggerFactory.getLogger(NotificationPanel.class);
    private final NotificationPanelConfiguration<FieldType, RepositoryType> configuration;
    private final VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreRegistry;
    private final ReflectionService<FieldType> reflectionService;

    private VortexCrudDataStore dataStore;
    private MessageList unreadList;
    private MessageList allList;
    private Div unreadContent;
    private Popover popover;

    public NotificationPanel(
            NotificationPanelConfiguration<FieldType, RepositoryType> configuration,
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreRegistry,
            ReflectionService<FieldType> reflectionService) {
        this.configuration = configuration;
        this.dataStoreRegistry = dataStoreRegistry;
        this.reflectionService = reflectionService;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // Get the data store
        dataStore = dataStoreRegistry.getDataStore(configuration.dataStoreKey());

        // Build the UI
        buildUI();

        // Load notifications
        loadNotifications();
    }

    private void buildUI() {
        // Bell icon button
        Button button = new Button(VaadinIcon.BELL.create());
        button.addThemeVariants(ButtonVariant.LUMO_ICON);
        button.setAriaLabel(configuration.ariaLabel());

        // Popover
        popover = new Popover();
        popover.setTarget(button);
        popover.setWidth("300px");
        popover.addThemeVariants(PopoverVariant.LUMO_NO_PADDING);
        popover.setPosition(PopoverPosition.BOTTOM);
        popover.setModal(true);
        popover.setAriaLabelledBy("notifications-heading");

        // Message lists
        unreadList = new MessageList();
        allList = new MessageList();

        // Tab sheet
        TabSheet tabSheet = new TabSheet();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_SMALL, TabSheetVariant.LUMO_NO_PADDING);
        tabSheet.addClassName("notifications");

        unreadContent = new Div();
        unreadContent.add(unreadList);

        tabSheet.add(getTranslation(configuration.unreadTabKey()), unreadContent);
        tabSheet.add(getTranslation(configuration.allTabKey()), new Div(allList));

        // Header
        H4 heading = new H4(getTranslation(configuration.headingKey()));
        heading.setId("notifications-heading");
        heading.getStyle().set("margin", "0");

        // Mark all read button
        Button markRead = new Button(getTranslation(configuration.markAllReadKey()), e -> markAllAsRead());
        markRead.getStyle().set("margin", "0 0 0 auto");
        markRead.addThemeVariants(ButtonVariant.LUMO_SMALL);

        HorizontalLayout layout = new HorizontalLayout(heading, markRead);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.getStyle().set("padding", "var(--lumo-space-m) var(--lumo-space-m) var(--lumo-space-xs)");

        popover.add(layout, tabSheet);

        add(button, popover);
    }

    private void loadNotifications() {
        if (dataStore == null) {
            return;
        }

        try {
            // Query notifications
            List<?> allNotifications = queryNotifications(false);
            List<?> unreadNotifications = queryNotifications(true);

            // Convert to MessageListItems
            List<MessageListItem> allItems = convertToMessageItems(allNotifications);
            List<MessageListItem> unreadItems = convertToMessageItems(unreadNotifications);

            // Update UI
            UI ui = getUI().orElse(null);
            if (ui != null) {
                ui.access(() -> {
                    allList.setItems(allItems);

                    if (unreadItems.isEmpty()) {
                        unreadContent.removeAll();
                        Div noNotifications = new Div(getTranslation(configuration.noNewNotificationsKey()));
                        noNotifications.addClassName("no-notifications-msg");
                        unreadContent.add(noNotifications);
                    } else {
                        unreadContent.removeAll();
                        unreadContent.add(unreadList);
                        unreadList.setItems(unreadItems);
                    }
                });
            }
        } catch (Exception e) {
            log.error("Error loading notifications", e);
        }
    }

    private List<?> queryNotifications(boolean unreadOnly) {
        List<?> notifications;
        int limit = configuration.limit();

        // If we need to filter by read status
        if (unreadOnly && configuration.readStatusField() != null) {
            // Query unread notifications
            if (configuration.filterField() != null && configuration.filterValue() != null) {
                // We need to filter by both read status and custom filter
                // Since we can't easily do compound queries, we'll query by filter and filter in memory
                notifications = dataStore.getRecordsFromTableWhereColumnEquals(
                    configuration.filterField(),
                    configuration.filterValue(),
                    0,
                    limit * 2  // Get more than needed to account for filtering
                );

                // Filter for unread in memory
                notifications = filterUnreadInMemory(notifications);
            } else {
                // Query only by read status
                notifications = dataStore.getRecordsFromTableWhereColumnEquals(
                    configuration.readStatusField(),
                    configuration.readStatusValueForUnread(),
                    0,
                    limit
                );
            }
        } else {
            // Query all notifications
            if (configuration.filterField() != null && configuration.filterValue() != null) {
                notifications = dataStore.getRecordsFromTableWhereColumnEquals(
                    configuration.filterField(),
                    configuration.filterValue(),
                    0,
                    limit
                );
            } else {
                notifications = dataStore.getRecordsFromTable(0, limit);
            }
        }

        // Limit the results
        if (notifications != null && notifications.size() > limit) {
            notifications = notifications.subList(0, limit);
        }

        return notifications != null ? notifications : new ArrayList<>();
    }

    private List<?> filterUnreadInMemory(List<?> notifications) {
        if (notifications == null || configuration.readStatusField() == null) {
            return notifications;
        }

        List<Object> unread = new ArrayList<>();
        for (Object notification : notifications) {
            Object readStatus = reflectionService.getValue(notification, configuration.readStatusField());
            if (configuration.readStatusValueForUnread().equals(readStatus)) {
                unread.add(notification);
            }
        }
        return unread;
    }

    private List<MessageListItem> convertToMessageItems(List<?> notifications) {
        List<MessageListItem> items = new ArrayList<>();

        for (Object notification : notifications) {
            try {
                // Extract message
                String message = "";
                if (configuration.messageField() != null) {
                    message = reflectionService.getString(notification, configuration.messageField());
                    if (message == null) {
                        message = "";
                    }
                }

                // Extract timestamp
                Instant timestamp = Instant.now();
                if (configuration.timestampField() != null) {
                    Object timestampValue = reflectionService.getValue(notification, configuration.timestampField());
                    timestamp = convertToInstant(timestampValue);
                }

                // Extract user name
                String userName = "";
                if (configuration.userNameField() != null) {
                    userName = reflectionService.getString(notification, configuration.userNameField());
                    if (userName == null) {
                        userName = "";
                    }
                }

                // Extract avatar URL
                String avatarUrl = null;
                if (configuration.userAvatarField() != null) {
                    avatarUrl = reflectionService.getString(notification, configuration.userAvatarField());
                }

                // Create message item
                MessageListItem item = new MessageListItem(message, timestamp, userName, avatarUrl);
                items.add(item);
            } catch (Exception e) {
                log.error("Error converting notification to message item", e);
            }
        }

        return items;
    }

    private Instant convertToInstant(Object timestampValue) {
        if (timestampValue == null) {
            return Instant.now();
        }

        if (timestampValue instanceof Instant) {
            return (Instant) timestampValue;
        }

        if (timestampValue instanceof LocalDateTime) {
            return ((LocalDateTime) timestampValue).toInstant(ZoneOffset.UTC);
        }

        if (timestampValue instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) timestampValue).toInstant();
        }

        if (timestampValue instanceof java.util.Date) {
            return ((java.util.Date) timestampValue).toInstant();
        }

        if (timestampValue instanceof Long) {
            return Instant.ofEpochMilli((Long) timestampValue);
        }

        return Instant.now();
    }

    private void markAllAsRead() {
        if (dataStore == null || configuration.readStatusField() == null) {
            return;
        }

        try {
            // Query unread notifications
            List<?> unreadNotifications = queryNotifications(true);

            // Mark each as read
            for (Object notification : unreadNotifications) {
                // Set read status to opposite of unread value (typically true if unread is false)
                Object readValue = !configuration.readStatusValueForUnread().equals(Boolean.FALSE);
                reflectionService.setValue(notification, configuration.readStatusField(), readValue);

                // Update in database
                dataStore.updateRecordById(notification);
            }

            // Refresh the UI
            loadNotifications();

            // Close the popover
            if (popover != null) {
                popover.close();
            }
        } catch (Exception e) {
            log.error("Error marking notifications as read", e);
        }
    }

    /**
     * Refresh the notification list from the data store
     */
    public void refresh() {
        loadNotifications();
    }
}
