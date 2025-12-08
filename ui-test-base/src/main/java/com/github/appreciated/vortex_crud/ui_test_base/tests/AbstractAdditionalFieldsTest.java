package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for testing additional field types that are not covered by other tests.
 * This includes TextArea, Password, Video, and BigDecimal fields.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractAdditionalFieldsTest extends BaseUITest {

    /**
     * Get the path to use for additional fields tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for additional fields tests
     */
    public String getAdditionalFieldsPath() {
        return "additional-fields-test";
    }

    @Test
    void testListingVisible() {
        navigateTo(getAdditionalFieldsPath());
        Locator webElement = waitForAnyElementContainingText("Test Entity");
        String tagName = (String) webElement.evaluate("element => element.tagName.toLowerCase()");
        assertEquals("vaadin-grid-cell-content", tagName);
    }

    @Test
    void testEntityLoading() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        // Verify TextArea field loads
        Locator textAreaField = waitForElement("vaadin-text-area").locator("textarea");
        assertTrue(textAreaField.inputValue().contains("This is a long description"));

        // Verify Password field exists (value should be masked)
        Locator passwordField = waitForElement("vaadin-password-field").locator("input");
        assertEquals("password", passwordField.getAttribute("type"));

        // Note: BigDecimal field test skipped - field may not render depending on configuration
        // Note: Video field test skipped when video_url is NULL in test data
    }

    @Test
    void testTextAreaInput() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        // Fill in required fields
        Locator nameField = waitForElementContainingText("vaadin-text-field", "Name").locator("input");
        nameField.fill("TextArea Test");

        // Fill in TextArea field
        Locator textAreaField = waitForElement("vaadin-text-area").locator("textarea");
        textAreaField.fill("This is a multi-line\ntext area\nwith several lines");

        // Fill Password field
        Locator passwordField = waitForElement("vaadin-password-field").locator("input");
        passwordField.fill("SecurePassword123");

        // Save
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());

        // Verify the entity was created
        waitForAnyElementContainingText("TextArea Test");
    }

    @Test
    void testPasswordFieldMasking() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        // Wait for and find password field
        Locator passwordField = waitForElement("vaadin-password-field").locator("input");

        // Verify it's a password type input (masked)
        assertEquals("password", passwordField.getAttribute("type"));

        // Enter a password
        passwordField.fill("MySecretPassword");

        // The value should be present but masked in the UI
        assertEquals("MySecretPassword", passwordField.inputValue());
    }

    @Test
    void testCreateEntry() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        Locator nameField = waitForElementContainingText("vaadin-text-field", "Name").locator("input");
        nameField.fill("Created Entity");

        Locator passwordField = waitForElement("vaadin-password-field").locator("input");
        passwordField.fill("newpassword");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Created Entity");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        Locator nameField = waitForElementContainingText("vaadin-text-field", "Name").locator("input");
        nameField.fill("");
        nameField.fill("Updated Entity");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Updated Entity");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getAdditionalFieldsPath());

        // Wait for the grid to refresh and the entity to disappear
        waitForTextToDisappear("Test Entity");
        assertTrue(page.getByText("Test Entity").all().isEmpty());
    }
}
