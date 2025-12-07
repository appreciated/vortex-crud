package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

/**
 * Base test for single form routes.
 */
public abstract class AbstractSingleFormRouteTest extends BaseUITest {

    protected String getSingleFormPath() {
        return "single-form-test";
    }

    @Test
    void testSingleFormRoute() {
        navigateTo(getSingleFormPath());
        // Should load entity with ID 1
        waitForAnyElementContainingText("Test Entity 1");
        // Should allow editing
        Locator nameField = waitForElementContainingText("vaadin-text-field", "Name").locator("input");
        nameField.fill("");
        nameField.fill("Updated via Single Form");
        waitForAnyElementContainingText("Save").click();

        // Reload to verify? Or just check if notification/success.
        // Since SingleFormRoute stays on the same page, we can reload.
        navigateTo(getSingleFormPath());
        waitForAnyElementContainingText("Updated via Single Form");
    }
}
