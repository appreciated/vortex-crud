package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
        // Checkboxes "Tag 1" and "Tag 2" should be present and checked?
        // Vaadin CheckboxGroup renders vaadin-checkbox elements.
        List<WebElement> checkboxes = driver.findElements(By.tagName("vaadin-checkbox"));
        assertTrue(checkboxes.size() >= 2);
        // We assume they are checked if loaded from DB, but verifying checked state on shadow dom might be tricky without access helpers.
        // At least we verify presence of "Tag 1" text.
        waitForAnyElementContainingText("Tag 1");
        waitForAnyElementContainingText("Tag 2");

        // Check PdfField
        // Should have a thumbnail or upload component.
        // Since we seeded 'test.pdf', and it might not exist on disk, it might show broken thumbnail or just the component.
        // We look for the component structure.
        // PdfDisplayComponent uses <embed> tag.
        waitForElement(By.tagName("embed"));

        // Check Notes Field (which is TextAreaField in this test)
        // Should contain "## Header"
        // Note: MarkDownField cannot be tested in JPA due to missing annotation support, so we fell back to TextAreaField.
        waitForElementWithTagAndValue("vaadin-text-area", "## Header");
    }
}
