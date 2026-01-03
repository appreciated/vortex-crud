package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class AbstractTextFieldTest extends BaseUITest {

    public String getValidationPath() {
        return "text-field-validation-test";
    }

    @Test
    void testTextFieldLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-text-field", "Test Value");
    }

    @Test
    void testRequiredFieldValidation() {
        navigateTo(getValidationPath());
        waitForButton("Create").click();

        // Try to save without filling required fields
        waitForButton("Save").click();

        // Check for validation error message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertThat(errorMessage).isVisible();

        // Find the required field by label text
        Locator requiredField = waitForElementContainingText("vaadin-text-field", "Required").locator("input");
        requiredField.fill("New Test Value");

        // Fill other required fields to verify this specific field's validation is satisfied
        // (We need to fill others to ensure we don't get stuck on them, but the focus here is the text field)
        page.locator("vaadin-email-field input").first().fill("valid@example.com");
        page.locator("vaadin-number-field input").first().fill("50.0");

        waitForButton("Save").click();
        waitForUrlToBe(getValidationPath());
    }
}
