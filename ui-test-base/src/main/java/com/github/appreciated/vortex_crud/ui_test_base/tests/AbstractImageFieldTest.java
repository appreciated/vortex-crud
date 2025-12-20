package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractImageFieldTest extends BaseUITest {

    public String getValidationPath() {
        return "field-validation-test";
    }

    @Test
    void testImageFieldLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");

        Locator image = waitForElement("img");
        assertTrue(image.getAttribute("src").contains("red.png"));
    }
}
