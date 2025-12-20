package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

public abstract class AbstractMissingFeaturesActionsTest extends BaseUITest {

    protected abstract String getPath();

    @Test
    void testCustomActions() {
        navigateTo(getPath());

        // Global Action "Print"
        waitForAnyElementContainingText("Print").click();
        waitForNotification("Global Action Executed");

        // Select an item to enable Single/Multi actions
        // Assuming checkboxes are present when actions requiring selection are added
        Locator checkbox = page.locator("vaadin-grid vaadin-checkbox").first();
        if (checkbox.isVisible()) {
            checkbox.click();

            // Single Entity Action
            waitForAnyElementContainingText("Single").click();
            waitForNotification("Single Action Executed");

            // Multi Entity Action
            waitForAnyElementContainingText("Multi").click();
            waitForNotification("Multi Action Executed");
        }
    }

    @Test
    void testMenuAction() {
        navigateTo(getPath());
        // Menu Action "Referenced Filter" - check for label
        waitForAnyElementContainingText("Referenced Filter");
    }

    private void waitForNotification(String text) {
        waitForElementContainingText("vaadin-notification-card", text);
    }
}
