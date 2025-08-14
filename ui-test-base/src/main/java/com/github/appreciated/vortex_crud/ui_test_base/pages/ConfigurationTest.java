package com.github.appreciated.vortex_crud.ui_test_base.pages;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UI tests for the configuration functionality.
 * Tests the UI components related to the basic_config_test.sql file.
 */
public class ConfigurationTest extends BaseUITest {

    @Test
    void testConfigListingVisible() {
        navigateTo("configurations");
        WebElement webElement = waitForAnyElementContainingText("max_connections");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
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
        WebElement valueField = waitForElementWithTagAndValue("vaadin-text-field", "30");
        valueField.clear();
        valueField.sendKeys("45");

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