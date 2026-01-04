package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEffect;
import com.vaadin.flow.component.button.Button;
import com.vaadin.signals.SignalFactory;
import com.vaadin.signals.ValueSignal;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.tabs.TabSheet;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
 */
public class NotificationPanel<ModelClass, FieldType> extends Div {

    private final ReflectionService<FieldType> reflection;
    private final NotificationPanelConfiguration<ModelClass, FieldType, ?> config;

    private final MessageList unreadList = new MessageList();
    private final MessageList allList = new MessageList();
    private final Div unreadContent = new Div();
    private final Popover popover = new Popover();

    public NotificationPanel(NotificationPanelConfiguration<ModelClass, FieldType, ?> config,
                             ReflectionService<FieldType> reflection) {
        this.config = config;
        this.reflection = reflection;
        buildUI();
    }

    private void buildUI() {
        // Bell button with ARIA label for testing
        Button bellBtn = new Button(VaadinIcon.BELL.create());
        bellBtn.addThemeVariants(ButtonVariant.LUMO_ICON);
        bellBtn.getElement().setAttribute("aria-label", config.ariaLabel());

        popover.setTarget(bellBtn);
        popover.setWidth("350px");
        popover.addThemeVariants(PopoverVariant.LUMO_NO_PADDING);

        // Header with localized title and mark-read button
        H4 title = new H4(getTranslation(config.headingKey()));
        title.getStyle().set("margin", "0");

        Button markReadBtn = new Button(getTranslation(config.markAllReadKey()), e -> markAllRead());
        markReadBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SMALL);

        HorizontalLayout header = new HorizontalLayout(title, markReadBtn);
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // Tabs with localized keys
        TabSheet tabs = new TabSheet();
        tabs.add(getTranslation(config.unreadTabKey()), unreadContent);
        tabs.add(getTranslation(config.allTabKey()), new Div(allList));

        popover.add(header, tabs);
        add(bellBtn, popover);
    }

    public void setData(List<ModelClass> items) {
        // Process "All" list
        allList.setItems(items.stream().map(this::mapToItem).toList());

        // Process "Unread" list
        List<MessageListItem> unreadItems = items.stream()
                .filter(this::isUnread)
                .map(this::mapToItem)
                .toList();

        updateUnreadTab(unreadItems);
    }

    private void updateUnreadTab(List<MessageListItem> items) {
        unreadContent.removeAll();
        if (items.isEmpty()) {
            // Test expects a specific class and localized text for empty states
            Span noNotifications = new Span(getTranslation(config.noNewNotificationsKey()));
            noNotifications.addClassName("no-notifications-msg");
            unreadContent.add(noNotifications);
        } else {
            unreadList.setItems(items);
            unreadContent.add(unreadList);
        }
    }

    private MessageListItem mapToItem(ModelClass entity) {
        String msg = reflection.getString(entity, config.messageField());
        Object tsValue = reflection.getValue(entity, config.timestampField());
        String user = config.userNameField() != null ? reflection.getString(entity, config.userNameField()) : null;
        String avatar = config.userAvatarField() != null ? reflection.getString(entity, config.userAvatarField()) : null;

        return new MessageListItem(
                msg != null ? msg : "",
                convertToInstant(tsValue),
                user != null ? user : "",
                avatar
        );
    }

    private boolean isUnread(ModelClass entity) {
        Object status = reflection.getValue(entity, config.readStatusField());
        return config.readStatusValueForUnread().equals(status);
    }

    private Instant convertToInstant(Object val) {
        if (val instanceof Instant i) return i;
        if (val instanceof LocalDateTime ldt) return ldt.toInstant(ZoneOffset.UTC);
        if (val instanceof java.util.Date d) return d.toInstant();
        if (val instanceof Long l) return Instant.ofEpochMilli(l);
        return Instant.now();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        refresh();
        subscribe();
    }

    private void subscribe() {
        if (config.dataStoreConfig() == null || config.dataStoreConfig().dataStoreInstance() == null) {
            return;
        }
        var dataStore = config.dataStoreConfig().dataStoreInstance();
        String topic = "entity-change:" + dataStore.getModelClass().getName();
        ValueSignal<Long> signal = SignalFactory.IN_MEMORY_SHARED.value(topic, Long.class);

        ComponentEffect.effect(this, () -> {
            // Register dependency on the signal value
            signal.value();
            // Refresh content
            refresh();
        });
    }

    public void refresh() {
        var dataStore = config.dataStoreConfig().dataStoreInstance();
        if (dataStore != null) {
            List<ModelClass> items = dataStore.getRecordsFromTable(0, config.limit());
            setData(items != null ? items : List.of());
        }
    }

    private void markAllRead() {
        var dataStore = config.dataStoreConfig().dataStoreInstance();
        if (dataStore == null || config.readStatusField() == null) return;

        try {
            List<ModelClass> items = dataStore.getRecordsFromTable(0, config.limit());
            if (items == null) return;

            Object readValue = config.readStatusValueForRead();

            for (ModelClass entity : items) {
                if (isUnread(entity)) {
                    reflection.setValue(entity, config.readStatusField(), readValue);
                    dataStore.updateRecordById(entity);
                }
            }

            refresh();
            popover.close(); // Closes as expected by test step 6
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("Error marking notifications as read", e);
        }
    }
}