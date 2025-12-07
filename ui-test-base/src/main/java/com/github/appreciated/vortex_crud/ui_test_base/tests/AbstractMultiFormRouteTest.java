package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for multi-form route tests.
 * Tests the MultiFormRoute functionality which displays multiple forms simultaneously
 * for a single entity.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractMultiFormRouteTest extends BaseUITest {

    /**
     * Get the path to use for multi-form tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for multi-form tests
     */
    public String getMultiFormPath() {
        return "multi-form-test";
    }

    @Test
    void testMultiFormListingVisible() {
        navigateTo(getMultiFormPath());
        Locator webElement = waitForAnyElementContainingText("Max Mustermann");
        String tagName = (String) webElement.evaluate("element => element.tagName.toLowerCase()");
        assertEquals("vaadin-grid-cell-content", tagName);
    }

    @Test
    void testMultiFormEntityLoading() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Max Mustermann").click();
        waitForUrlToBe(getMultiFormPath() + "/1");

        // Verify first form fields (Basic Information)
        waitForElementWithTagAndValue("vaadin-text-field", "Max Mustermann");
        waitForElementWithTagAndValue("vaadin-email-field", "profile@example.com");

        // Verify second form fields (Additional Details)
        // Use Locator to check textarea value if needed, or helper
        // BaseUITest.waitForElementWithTagAndValue uses starts-with(@value).
        // For textarea in vaadin-text-area, value attribute might not be on host.
        // Assuming helper works or fallback to check inputValue.
        // Let's rely on helper if it works, or fix helper.
        // But for safety I'll assume helper works or I fix it in BaseUITest if I find it broken.
        // Wait, for AbstractFormSlideTest I changed helper to locator("textarea").inputValue().
        // Here I am reusing waitForElementWithTagAndValue.
        // I should check if waitForElementWithTagAndValue works for vaadin-text-area.
        // vaadin-text-area usually reflects value to property, not attribute.
        // I'll leave it for now, assuming logic is similar to selenium test which passed (maybe).
        // Actually Selenium test used waitForElementWithTagAndValue("vaadin-text-area", ...).

        // I'll use explicit check for textarea to be safe.
        Locator textArea = waitForElement("vaadin-text-area").locator("textarea");
        assertTrue(textArea.inputValue().contains("This is a profile description"));

        waitForElementWithTagAndValue("vaadin-integer-field", "25");
    }

    @Test
    void testMultiFormRequiredFieldValidation() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Create").click();

        // Try to save without filling required fields
        waitForAnyElementContainingText("Save").click();

        // Check for validation error message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertTrue(errorMessage.isVisible());

        // Fill required field from first form
        List<Locator> textFields = waitForElements("vaadin-text-field");
        Locator nameField = textFields.get(0).locator("input");
        nameField.fill("New Profile");

        // Fill required email field
        Locator emailField = page.locator("vaadin-email-field input").first();
        emailField.fill("newprofile@example.com");

        // Try to save again
        waitForAnyElementContainingText("Save").click();

        // Should navigate back to list view if validation passes
        waitForUrlToBe(getMultiFormPath());
    }

    @Test
    void testMultiFormCreateEntry() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Create").click();

        // Fill fields from first form (Basic Information)
        Locator nameField = waitForElement("//vaadin-dialog//vaadin-text-field").locator("input");
        nameField.fill("Created Profile");

        Locator emailField = page.locator("vaadin-email-field input").first();
        emailField.fill("created@example.com");

        // Fill fields from second form (Additional Details)
        Locator descriptionField = page.locator("vaadin-text-area textarea").first();
        descriptionField.fill("This is a created profile");

        Locator ageField = page.locator("vaadin-integer-field input").first();
        ageField.fill("30");

        waitForAnyElementContainingText("Save").click();

        waitForUrlToBe(getMultiFormPath());
        waitForAnyElementContainingText("Created Profile");
    }

    @Test
    void testMultiFormUpdateEntry() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Max Mustermann").click();
        waitForUrlToBe(getMultiFormPath() + "/1");

        // Update field from first form
        Locator nameField = waitForElement("vaadin-text-field").locator("input");
        nameField.fill("");
        nameField.fill("Updated Profile");

        // Update field from second form
        Locator ageField = page.locator("vaadin-integer-field input").first();
        ageField.fill("");
        ageField.fill("35");

        waitForAnyElementContainingText("Save").click();

        waitForUrlToBe(getMultiFormPath());
        waitForAnyElementContainingText("Updated Profile");
    }

    @Test
    void testMultiFormDeleteEntry() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Max Mustermann").click();
        waitForUrlToBe(getMultiFormPath() + "/1");

        waitForAnyElementContainingText("Delete").click();

        waitForUrlToBe(getMultiFormPath());
        List<Locator> elements = page.locator("//*[contains(text(), 'Max Mustermann')]").all();
        assertTrue(elements.stream().noneMatch(this::isDisplayedSafe));
    }

    @Test
    void testMultiFormFieldsAcrossForms() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Create").click();

        // Fill all fields across both forms
        Locator nameField = waitForElement("//vaadin-dialog//vaadin-text-field").locator("input");
        nameField.fill("Complete Profile");

        Locator emailField = page.locator("vaadin-email-field input").first();
        emailField.fill("complete@example.com");

        Locator descriptionField = page.locator("vaadin-text-area textarea").first();
        descriptionField.fill("Complete profile description");

        Locator ageField = page.locator("vaadin-integer-field input").first();
        ageField.fill("28");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getMultiFormPath());

        // Verify all data was saved
        waitForAnyElementContainingText("Complete Profile").click();
        waitForUrlToBe(getMultiFormPath() + "/2");

        // Verify first form data
        waitForElementWithTagAndValue("vaadin-text-field", "Complete Profile");
        waitForElementWithTagAndValue("vaadin-email-field", "complete@example.com");

        // Verify second form data
        Locator textArea = waitForElement("vaadin-text-area").locator("textarea");
        assertTrue(textArea.inputValue().contains("Complete profile description"));
        waitForElementWithTagAndValue("vaadin-integer-field", "28");
    }
}
