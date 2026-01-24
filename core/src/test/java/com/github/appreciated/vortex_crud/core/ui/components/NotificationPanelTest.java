package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.popover.Popover;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class NotificationPanelTest {

    @Mock
    private NotificationPanelConfiguration<Object, String, Object> config;

    @Mock
    private ReflectionService<String> reflection;

    private NotificationPanel<Object, String> panel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock default behaviors
        when(config.ariaLabel()).thenReturn("Notifications");
        when(config.headingKey()).thenReturn("notifications.heading");
        when(config.markAllReadKey()).thenReturn("notifications.mark_all_read");
        when(config.unreadTabKey()).thenReturn("notifications.unread");
        when(config.allTabKey()).thenReturn("notifications.all");
        when(config.noNewNotificationsKey()).thenReturn("notifications.no_new");

        when(config.messageField()).thenReturn("message");
        when(config.timestampField()).thenReturn("timestamp");
        when(config.readStatusField()).thenReturn("read");
        when(config.readStatusValueForUnread()).thenReturn(false);
        when(config.readStatusValueForRead()).thenReturn(true);
    }

    @Test
    void testBellVisibilityWithUnread() {
        panel = new TestNotificationPanel(config, reflection);

        Object unreadEntity = new Object();
        when(reflection.getValue(unreadEntity, "read")).thenReturn(false);
        when(reflection.getString(unreadEntity, "message")).thenReturn("Unread");

        panel.setData(List.of(unreadEntity));

        Button bellBtn = findBellButton(panel);
        assertTrue(bellBtn.isVisible(), "Bell button should be visible when there are unread items");
    }

    @Test
    void testBellVisibilityWithoutUnread() {
        panel = new TestNotificationPanel(config, reflection);

        Object readEntity = new Object();
        when(reflection.getValue(readEntity, "read")).thenReturn(true);
        when(reflection.getString(readEntity, "message")).thenReturn("Read");

        panel.setData(List.of(readEntity));

        Button bellBtn = findBellButton(panel);
        assertFalse(bellBtn.isVisible(), "Bell button should be hidden when there are no unread items");
    }

    @Test
    void testShowAllButtonPresent() {
        when(config.showAllRoute()).thenReturn("notifications");
        panel = new TestNotificationPanel(config, reflection);

        Popover popover = findPopover(panel);
        assertNotNull(popover, "Popover should exist");

        Button showAllBtn = findShowAllButton_Robust(popover);
        assertNotNull(showAllBtn, "Show All button should be present in popover");
    }

    @Test
    void testShowAllButtonAbsent() {
        when(config.showAllRoute()).thenReturn(null);
        panel = new TestNotificationPanel(config, reflection);

        Popover popover = findPopover(panel);
        assertNotNull(popover, "Popover should exist");

        Button showAllBtn = findShowAllButton_Robust(popover);
        assertNull(showAllBtn, "Show All button should NOT be present in popover");
    }

    private Button findBellButton(NotificationPanel panel) {
        return (Button) panel.getChildren()
                .filter(c -> c instanceof Button)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Bell button not found"));
    }

    private Popover findPopover(NotificationPanel panel) {
        return (Popover) panel.getChildren()
                .filter(c -> c instanceof Popover)
                .findFirst()
                .orElse(null);
    }

    private Button findShowAllButton_Robust(Popover popover) {
        List<Component> children = popover.getChildren().collect(Collectors.toList());
        // Expected structure: Header (HL), Tabs, Footer (HL) (optional)

        for (Component child : children) {
            if (child instanceof HorizontalLayout) {
                HorizontalLayout layout = (HorizontalLayout) child;
                // Header has H4 and Button.
                // Footer has Button.
                long buttonCount = layout.getChildren().filter(c -> c instanceof Button).count();
                long h4Count = layout.getChildren().filter(c -> c instanceof H4).count();

                if (buttonCount == 1 && h4Count == 0) {
                    return (Button) layout.getChildren().findFirst().get();
                }
            }
        }
        return null;
    }

    private static class TestNotificationPanel extends NotificationPanel<Object, String> {
        public TestNotificationPanel(NotificationPanelConfiguration<Object, String, ?> config, ReflectionService<String> reflection) {
            super(config, reflection);
        }

        @Override
        public String getTranslation(String key, Object... params) {
            return key;
        }
    }
}
