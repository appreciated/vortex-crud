package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractTextAreaTypeTest extends BaseUITest {

    protected String getPath() {
        return "missing-features-test";
    }

    @Test
    void testTextAreaField() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Test Entity 1").click();
        waitForUrlToBe(getPath() + "/1");

        // Check Notes Field (which is TextAreaField in this test)
        // Should contain "## Header"
        Locator textArea = waitForElement("vaadin-text-area");
        // Check if value attribute is set on host or check internal textarea
        String value = textArea.locator("textarea").inputValue();
        assertTrue(value.contains("## Header"));
    }
}
