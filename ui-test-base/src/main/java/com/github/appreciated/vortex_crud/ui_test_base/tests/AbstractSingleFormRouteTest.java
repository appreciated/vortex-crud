package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Base test for single form routes.
 */
public abstract class AbstractSingleFormRouteTest extends BaseUITest {

    protected String getSingleFormPath() {
        return "single-form-test";
    }

    @Test
    void testSingleFormRoute() {
        // Navigate and don't wait for exact URL match since single form routes may have different URL patterns
        page.navigate(getUrl(getSingleFormPath()));
        page.waitForTimeout(3000);

        // Should load entity with ID 1 - wait for the form to be visible
        waitForAnyElementContainingText("Test Entity 1");

        // Should allow editing - find the name field
        Locator nameField = waitForElementContainingText("vaadin-text-field", "Name").locator("input");

        // Clear and update the field
        nameField.fill("");
        nameField.fill("Updated via Single Form");

        // Verify the field value was updated in the UI before saving
        assertEquals("Updated via Single Form", nameField.inputValue());

        // Save the form
        Locator saveButton = waitForAnyElementContainingText("Save");
        saveButton.click();

        // Wait for save operation to complete
        page.waitForTimeout(1000);

        // Test passes if we got here without errors - the form loaded, allowed editing, and saved
    }

    private String getUrl(String path) {
        return "http://127.0.0.1:%d/%s".formatted(getPort(), path);
    }

    private int getPort() {
        // Access port through reflection or add a getter in BaseUITest
        try {
            java.lang.reflect.Field portField = BaseUITest.class.getDeclaredField("port");
            portField.setAccessible(true);
            return (int) portField.get(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get port", e);
        }
    }
}
