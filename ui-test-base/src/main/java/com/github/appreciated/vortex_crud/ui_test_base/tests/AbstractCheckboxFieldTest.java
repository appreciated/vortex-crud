package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;

public abstract class AbstractCheckboxFieldTest extends BaseUITest {

    public String getValidationPath() {
        return "checkbox-field-validation-test";
    }

    @Test
    void testCheckboxFieldLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-checkbox", "on");
    }

    @Test
    void testCheckboxInput() {
        navigateTo(getValidationPath());
        waitForButton("Create").click();

        // Fill required fields
        waitForElementContainingText("vaadin-text-field", "Required").locator("input").fill("Checkbox Test");
        page.locator("vaadin-email-field input").first().fill("checkbox@example.com");
        page.locator("vaadin-number-field input").first().fill("10.0");

        waitForElement("vaadin-checkbox").click();

        waitForButton("Save").click();
        waitForUrlToBe(getValidationPath());
    }
}
