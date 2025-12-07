package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for field validation tests.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractFieldValidationTest extends BaseUITest {

    /**
     * Get the path to use for validation tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for validation tests
     */
    public String getValidationPath() {
        return "field-validation-test";
    }

    @Test
    void testValidationListingVisible() {
        navigateTo(getValidationPath());
        Locator webElement = waitForAnyElementContainingText("Test Value");
        String tagName = (String) webElement.evaluate("element => element.tagName.toLowerCase()");
        assertEquals("vaadin-grid-cell-content", tagName);
    }

    @Test
    void testValidationEntityLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-text-field", "Test Value");
        waitForElementWithTagAndValue("vaadin-email-field", "test@example.com");
        waitForElementWithTagAndValue("vaadin-number-field", "42");
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-01-01");
        waitForElementWithTagAndValue("vaadin-date-time-picker//vaadin-date-picker", "2023-01-01");
        waitForElementWithTagAndValue("vaadin-date-time-picker//vaadin-time-picker", "10:15");
        waitForElementWithTagAndValue("vaadin-select", "1");
        waitForElementWithTagAndValue("vaadin-checkbox", "on");
        Locator image = waitForElement("img");
        assertTrue(image.getAttribute("src").contains("red.png"));
    }

    @Test
    void testRequiredFieldValidation() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Create").click();

        // Try to save without filling required fields
        waitForAnyElementContainingText("Save").click();

        // Check for validation error message - look for the specific constraint violation message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertTrue(errorMessage.isVisible());

        // Find the required field by label text instead of required attribute
        Locator requiredField = waitForElementContainingText("vaadin-text-field", "Required").locator("input");
        requiredField.fill("New Test Value");

        // Fill email field (second text field based on HTML)
        Locator emailField = page.locator("vaadin-email-field input").first();
        emailField.fill("new@example.com");

        // Fill numeric field with Double value to avoid ClassCastException
        Locator numericField = page.locator("vaadin-number-field input").first();
        numericField.fill("50.0");

        // Try to save again
        waitForAnyElementContainingText("Save").click();

        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }

    @Test
    void testEmailValidation() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Create").click();

        // Fill the required field by finding the field with "Required" label
        Locator requiredField = waitForElementContainingText("vaadin-text-field", "Required").locator("input");
        requiredField.fill("Email Test");

        // Fill email field with invalid value (second text field based on HTML)
        Locator emailField = page.locator("vaadin-email-field input").first();
        emailField.fill("invalid-email");

        // Fill numeric field with valid Double value
        Locator numericField = page.locator("vaadin-number-field input").first();
        numericField.fill("50.0");

        // Try to save
        waitForAnyElementContainingText("Save").click();

        // Check for validation error message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertTrue(errorMessage.isVisible());

        // Correct the email
        emailField.fill("valid@example.com");

        // Try to save again
        waitForAnyElementContainingText("Save").click();

        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }

    @Test
    void testNumericValidation() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Create").click();

        // Fill required field
        Locator requiredField = waitForElementContainingText("vaadin-text-field", "Required").locator("input");
        requiredField.fill("Numeric Test");

        // Fill email field (second text field)
        Locator emailField = page.locator("vaadin-email-field input").first();
        emailField.fill("numeric@example.com");

        // Fill numeric field with invalid value (negative number)
        Locator numericField = page.locator("vaadin-number-field input").first();
        numericField.fill("-5.0");

        // Try to save
        waitForAnyElementContainingText("Save").click();

        // Check for validation error message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some field");
        assertTrue(errorMessage.isVisible());

        // Correct the numeric value
        numericField.fill("25.0");

        // Try to save again
        waitForAnyElementContainingText("Save").click();

        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }

    @Test
    void testDateTimeAndCheckboxInput() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Create").click();

        Locator requiredField = waitForElementContainingText("vaadin-text-field", "Required").locator("input");
        requiredField.fill("DateTime Test");

        Locator emailField = page.locator("vaadin-email-field input").first();
        emailField.fill("datetime@example.com");

        Locator numericField = page.locator("vaadin-number-field input").first();
        numericField.fill("10.0");

        Locator dateTimeField = page.locator("vaadin-date-time-picker input").first();
        dateTimeField.fill("2024-01-01T12:00");
        dateTimeField.press("Escape"); // manual closing needed as the date time picker popup hides other elements

        waitForElement("vaadin-checkbox").click();

        waitForAnyElementContainingText("Save").click();

        waitForUrlToBe(getValidationPath());
    }

    @Test
    void testCreateEntry() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Create").click();

        Locator requiredField = waitForElementContainingText("vaadin-text-field", "Required").locator("input");
        requiredField.fill("Created Value");

        waitForAnyElementContainingText("Save").click();

        waitForUrlToBe(getValidationPath());
        waitForAnyElementContainingText("Created Value");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");

        Locator requiredField = waitForElementContainingText("vaadin-text-field", "Required").locator("input");
        requiredField.fill("Updated Value");

        waitForAnyElementContainingText("Save").click();

        waitForUrlToBe(getValidationPath());
        waitForAnyElementContainingText("Updated Value");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");

        waitForAnyElementContainingText("Delete").click();

        waitForUrlToBe(getValidationPath());
        List<Locator> elements = page.locator("//*[contains(text(), 'Test Value')]").all();

    }
}
