package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for submenu routes, verifying nested navigation support.
 */
public abstract class AbstractSubmenuRouteTest extends BaseUITest {

    protected String getSubmenuPath() {
        return "submenu-test";
    }

    @Test
    void testDeeplyNestedSettings() {
        // 1. Navigate to the top-level submenu
        page.navigate(getUrl(getSubmenuPath()));
        page.waitForTimeout(1000);

        // Verify top-level menu items are present
        waitForAnyElementContainingText("General Settings");
        waitForAnyElementContainingText("Advanced Settings");

        // 2. Click on "Advanced Settings" (which is a nested Submenu)
        clickMenuCard("Advanced Settings");
        // Wait for URL to update
        page.waitForURL(Pattern.compile(".*advanced"));

        // Verify nested menu items are present in the detail view
        // Since Submenu renders children in a list on the left, and detail on the right
        // The nested submenu should appear in the detail area.
        // We look for the nested items.
        waitForAnyElementContainingText("Security");
        waitForAnyElementContainingText("Profile");

        // 3. Click on "Profile" (which is a SingleFormRoute)
        clickMenuCard("Profile");
        // Wait for URL to update
        page.waitForURL(Pattern.compile(".*profile"));

        // Verify the SingleFormRoute is rendered (e.g. check for a specific field label)
        waitForElementContainingText("vaadin-text-field", "Username");

        // 4. Verify we can interact with the form
        Locator usernameField = waitForElementContainingText("vaadin-text-field", "Username").locator("input");
        usernameField.fill("NestedUser");
    }

    private void clickMenuCard(String title) {
        // Find the card with the given title and click it
        // The structure in Submenu.java is Card -> H4(title)
        Locator card = page.locator("vaadin-card").filter(new Locator.FilterOptions().setHasText(title)).first();
        card.click();
    }

    private String getUrl(String path) {
        return "http://127.0.0.1:%d/%s".formatted(getPort(), path);
    }

    private int getPort() {
        try {
            java.lang.reflect.Field portField = BaseUITest.class.getDeclaredField("port");
            portField.setAccessible(true);
            return (int) portField.get(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get port", e);
        }
    }
}
