package com.github.appreciated.vortex_crud.ui_test_base.pages;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UI tests for the configuration functionality.
 * Tests the UI components related to the basic_config_test.sql file.
 */
public class ConfigurationTest extends BaseUITest {

    @Test
    void testConfigListingVisible() {
        navigateTo("configurations");
        Locator webElement = waitForAnyElementContainingText("max_connections");
        String tagName = (String) webElement.evaluate("element => element.tagName.toLowerCase()");
        assertEquals("vaadin-grid-cell-content", tagName);
    }

    @Test
    void testConfigNavigationPossible() {
        navigateTo("configurations");
        waitForAnyElementContainingText("max_connections").click();
        waitForUrlToBe("configurations/1");
        waitForElementWithTagAndValue("vaadin-text-field", "max_connections");
        waitForElementWithTagAndValue("vaadin-text-field", "100");
        waitForElementWithTagAndValue("vaadin-checkbox", "true");
    }

    @Test
    void testConfigEditing() {
        navigateTo("configurations");
        waitForAnyElementContainingText("timeout_seconds").click();
        waitForUrlToBe("configurations/2");

        // Find the value field and clear it
        Locator valueField = waitForElementWithTagAndValue("vaadin-text-field", "30");
        valueField.fill("45");

        // Click save button
        waitForAnyElementContainingText("Save").click();

        // Verify we're back at the list view
        waitForUrlToBe("configurations");

        // Navigate back to the edited item
        waitForAnyElementContainingText("timeout_seconds").click();
        waitForUrlToBe("configurations/2");

        // Verify the value was updated
        waitForElementWithTagAndValue("vaadin-text-field", "45");
    }
}
