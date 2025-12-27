package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class AbstractSelectFieldValidationTest extends BaseUITest {

    public String getValidationPath() {
        return "field-validation-test";
    }

    @Test
    void testSelectFieldLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-select", "1");
    }
}
