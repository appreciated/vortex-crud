package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractMultiSelectValueFieldTest extends BaseUITest {

    protected String getPath() {
        return "missing-features-test";
    }

    @Test
    void testMultiSelectField() {
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
    }
}
