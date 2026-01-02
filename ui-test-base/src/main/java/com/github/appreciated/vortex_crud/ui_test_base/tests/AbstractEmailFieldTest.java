package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class AbstractEmailFieldTest extends BaseUITest {

    public String getValidationPath() {
        return "email-field-validation-test";
    }

    @Test
    void testEmailFieldLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-email-field", "test@example.com");
    }

    @Test
    void testEmailValidation() {
        navigateTo(getValidationPath());
        waitForButton("Create").click();

        // Fill required text field
        waitForElementContainingText("vaadin-text-field", "Required").locator("input").fill("Email Test");
        // Fill numeric field
        page.locator("vaadin-number-field input").first().fill("50.0");

        // Fill email field with invalid value
        Locator emailField = page.locator("vaadin-email-field input").first();
        emailField.fill("invalid-email");

        waitForButton("Save").click();

        // Check for validation error message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertThat(errorMessage).isVisible();

        // Correct the email
        emailField.fill("valid@example.com");

        waitForButton("Save").click();
        waitForUrlToBe(getValidationPath());
    }
}
