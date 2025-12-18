package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class AbstractFieldValidationLogicTest extends BaseUITest {

    public String getValidationPath() {
        return "field-validation-test";
    }

    @Test
    void testRequiredFieldValidation() {
        navigateTo(getValidationPath());
        waitForButton("Create").click();

        // Try to save without filling required fields
        waitForButton("Save").click();

        // Check for validation error message - look for the specific constraint violation message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertThat(errorMessage).isVisible();

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
        waitForButton("Save").click();

        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }

    @Test
    void testEmailValidation() {
        navigateTo(getValidationPath());
        waitForButton("Create").click();

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
        waitForButton("Save").click();

        // Check for validation error message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertThat(errorMessage).isVisible();

        // Correct the email
        emailField.fill("valid@example.com");

        // Try to save again
        waitForButton("Save").click();

        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }

    @Test
    void testNumericValidation() {
        navigateTo(getValidationPath());
        waitForButton("Create").click();

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
        waitForButton("Save").click();

        // Check for validation error message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some field");
        assertThat(errorMessage).isVisible();

        // Correct the numeric value
        numericField.fill("25.0");

        // Try to save again
        waitForButton("Save").click();

        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }
}
