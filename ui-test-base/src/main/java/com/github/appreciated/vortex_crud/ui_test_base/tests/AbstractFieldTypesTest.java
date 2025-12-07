package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for various field types (MultiSelectValueField, PdfField, TextAreaField).
 */
public abstract class AbstractFieldTypesTest extends BaseUITest {

    protected String getPath() {
        return "missing-features-test";
    }

    @Test
    void testEntityLoadingAndFields() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Test Entity 1").click();
        waitForUrlToBe(getPath() + "/1");

        // Check MultiSelectValueField (CheckboxGroup)
        if (page.locator("//*[contains(text(), 'Tags')]").count() > 0) {
            List<Locator> checkboxes = page.locator("vaadin-checkbox").all();
            assertTrue(checkboxes.size() >= 2);
            waitForAnyElementContainingText("Tag 1");
            waitForAnyElementContainingText("Tag 2");
        }

        // Check PdfField
        waitForAnyElementContainingText("PDF");

        // Check Notes Field (which is TextAreaField in this test)
        // Should contain "## Header"
        Locator textArea = waitForElement("vaadin-text-area");
        // Check if value attribute is set on host or check internal textarea
        String value = textArea.locator("textarea").inputValue();
        assertTrue(value.contains("## Header"));
    }
}
