package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
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
        Locator bellIcon = page.getByLabel("Notifications");
        assertThat(bellIcon).isVisible();

        // 2. Open the panel
        bellIcon.click();
        Locator locator = page.locator("vaadin-popover-overlay");
        assertThat(locator).isVisible();

        // 3. Verify Tabs are present (Unread and All)
        // Tab names are localized, but typically "Unread" and "All".
        // The implementation uses keys "notifications.unread" and "notifications.all" by default.
        // In ui_test_i18n, we might need to check the actual translated text.
        // We can use a more robust locator if text varies.
        assertThat(page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Unread"))).isVisible();
        assertThat(page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("All"))).isVisible();

        // 4. Verify Notifications are present
        // We expect some unread notifications seeded by the test
        Locator messageList = page.locator("vaadin-message-list");
        assertThat(messageList).isVisible();

        // Check for specific message content from seeded data
        // Example: "New task assigned"
        Locator unreadTab = page.getByRole(AriaRole.TABPANEL, new Page.GetByRoleOptions().setName("Unread"));
        assertThat(unreadTab.locator("vaadin-message >> text=New task assigned")).isVisible();

        // 5. Switch to All tab
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("All")).click();
        Locator allTab = page.getByRole(AriaRole.TABPANEL, new Page.GetByRoleOptions().setName("All"));
        assertThat(allTab.locator("vaadin-message >> text=New task assigned")).isVisible();

        // 6. Test "Mark all as read"
        // Button text key "notification.panel.mark.all.read" -> "Mark all as read"
        Locator markReadBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mark all as read"));
        assertThat(markReadBtn).isVisible();
        markReadBtn.click();

        // After clicking mark read, popover usually closes or list updates.
        // Implementation says: "Refresh the UI" and "Close the popover".
        assertThat(locator).not().isVisible();

        // Re-open and check if unread is empty
        bellIcon.click();
        assertThat(locator).isVisible();

        // Verify "Mark all as read" is disabled
        assertThat(markReadBtn).isDisabled();

        // key "notification.panel.no.notifications" -> "No new notifications"
        // It has class "no-notifications-msg"
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Unread")).click();
        assertThat(page.getByText("No new notifications").first()).isVisible();
    }
}
