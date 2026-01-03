package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class AbstractDateTimeFieldTest extends BaseUITest {

    public String getValidationPath() {
        return "datetime-field-validation-test";
    }

    @Test
    void testDateTimeFieldLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");

        // For date-time-picker, just verify the component exists
        Locator dateTimePicker = waitForElement("vaadin-date-time-picker");
        assertThat(dateTimePicker).isVisible();
    }

    @Test
    void testDateTimeInput() {
        navigateTo(getValidationPath());
        waitForButton("Create").click();

        // Fill required fields
        waitForElementContainingText("vaadin-text-field", "Required").locator("input").fill("DateTime Test");
        page.locator("vaadin-email-field input").first().fill("datetime@example.com");
        page.locator("vaadin-number-field input").first().fill("10.0");

        Locator dateTimeField = page.locator("vaadin-date-time-picker input").first();
        dateTimeField.fill("2024-01-01T12:00");
        dateTimeField.press("Escape");

        waitForButton("Save").click();
        waitForUrlToBe(getValidationPath());
    }
}
