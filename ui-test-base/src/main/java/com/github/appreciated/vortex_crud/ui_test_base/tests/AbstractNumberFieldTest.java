package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class AbstractNumberFieldTest extends BaseUITest {

    public String getValidationPath() {
        return "number-field-validation-test";
    }

    @Test
    void testNumberFieldLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-number-field", "42");
    }

    @Test
    void testNumericValidation() {
        navigateTo(getValidationPath());
        waitForButton("Create").click();

        // Fill required text field
        waitForElementContainingText("vaadin-text-field", "Required").locator("input").fill("Numeric Test");
        // Fill email field
        page.locator("vaadin-email-field input").first().fill("numeric@example.com");

        // Fill numeric field with invalid value (negative number)
        Locator numericField = page.locator("vaadin-number-field input").first();
        numericField.fill("-5.0");

        waitForButton("Save").click();

        // Check for validation error message
        Locator errorMessage = waitForAnyElementContainingText("Validation has failed for some field");
        assertThat(errorMessage).isVisible();

        // Correct the numeric value
        numericField.fill("25.0");

        waitForButton("Save").click();
        waitForUrlToBe(getValidationPath());
    }
}
