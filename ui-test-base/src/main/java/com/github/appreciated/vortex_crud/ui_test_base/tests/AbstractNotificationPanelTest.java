package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Base test for Notification Panel.
 */
public abstract class AbstractNotificationPanelTest extends BaseUITest {

    @Test
    void testNotificationPanelVisibleAndFunctional() {
        navigateTo(""); // Navigate to root or any page

        // 1. Verify Bell Icon is visible
        Locator bellIcon = page.locator("vaadin-button[title='Notifications']");
        assertThat(bellIcon).isVisible();

        // 2. Open the panel
        bellIcon.click();
        Locator popover = page.locator("vaadin-popover-overlay");
        assertThat(popover).isVisible();

        // 3. Verify Tabs are present (Unread and All)
        // Tab names are localized, but typically "Unread" and "All".
        // The implementation uses keys "notification.panel.unread" and "notification.panel.all".
        // We can assume default English text "Unread" and "All".
        assertThat(popover.locator("vaadin-tab >> text=Unread")).isVisible();
        assertThat(popover.locator("vaadin-tab >> text=All")).isVisible();

        // 4. Verify Notifications are present
        // We expect some unread notifications seeded by the test
        Locator messageList = popover.locator("vaadin-message-list");
        assertThat(messageList).isVisible();

        // Check for specific message content from seeded data
        // Example: "New task assigned"
        assertThat(popover.locator("vaadin-message >> text=New task assigned")).isVisible();

        // 5. Switch to All tab
        popover.locator("vaadin-tab >> text=All").click();
        assertThat(popover.locator("vaadin-message >> text=New task assigned")).isVisible();

        // 6. Test "Mark all as read"
        // Button text key "notification.panel.mark.all.read" -> "Mark all as read"
        Locator markReadBtn = popover.locator("vaadin-button >> text=Mark all as read");
        assertThat(markReadBtn).isVisible();
        markReadBtn.click();

        // After clicking mark read, popover usually closes or list updates.
        // Implementation says: "Refresh the UI" and "Close the popover".
        assertThat(popover).not().isVisible();

        // Re-open and check if unread is empty
        bellIcon.click();
        assertThat(popover).isVisible();

        // Check for "No new notifications" message
        // Key "notification.panel.no.notifications" -> "No new notifications"
        // It has class "no-notifications-msg"
        assertThat(popover.locator(".no-notifications-msg")).isVisible();
    }
}
