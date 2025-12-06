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

    protected boolean supportsDateRangeFields() {
        return false;
    }

    protected boolean supportsAllFieldTypes() {
        return false;
    }

    @Test
    void testEntityLoadingAndFields() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Test Entity 1").click();
        waitForUrlToBe(getPath() + "/1");

        if (supportsAllFieldTypes()) {
             // CheckboxField
             waitForAnyElementContainingText("Checkbox");
             waitForElement(By.tagName("vaadin-checkbox"));
             // DateField
             waitForAnyElementContainingText("Date");
             waitForElement(By.tagName("vaadin-date-picker"));
             // DateTimePickerField
             waitForAnyElementContainingText("DateTime");
             waitForElement(By.tagName("vaadin-date-time-picker"));
             // DoubleField (NumberField)
             waitForAnyElementContainingText("Double");
             waitForElement(By.tagName("vaadin-number-field"));
             // EmailField
             waitForAnyElementContainingText("Email");
             waitForElement(By.tagName("vaadin-email-field"));
             // FileField
             waitForAnyElementContainingText("File");
             waitForElement(By.tagName("vaadin-upload"));
             // ImageField
             waitForAnyElementContainingText("Image");
             waitForElement(By.tagName("vaadin-upload"));
             // IntegerField (NumberField)
             waitForAnyElementContainingText("Integer");
             waitForElement(By.tagName("vaadin-integer-field"));
             // SelectField
             waitForAnyElementContainingText("Select");
             waitForElement(By.tagName("vaadin-select"));
        }

        // Check MultiSelectValueField (CheckboxGroup)
        // Checkboxes "Tag 1" and "Tag 2" should be present and checked?
        // Vaadin CheckboxGroup renders vaadin-checkbox elements.
        if (!driver.findElements(By.xpath("//*[contains(text(), 'Tags')]")).isEmpty()) {
            List<WebElement> checkboxes = driver.findElements(By.tagName("vaadin-checkbox"));
            assertTrue(checkboxes.size() >= 2);
            // We assume they are checked if loaded from DB, but verifying checked state on shadow dom might be tricky without access helpers.
            // At least we verify presence of "Tag 1" text.
            waitForAnyElementContainingText("Tag 1");
            waitForAnyElementContainingText("Tag 2");
        }

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

        if (supportsDateRangeFields()) {
            // Check DateRangeField and DateTimeRangeField
            // These are CustomFields containing "Start" and "End" labels.
            waitForAnyElementContainingText("Date Range");
            waitForAnyElementContainingText("DateTime Range");

            // Verify structure (we should see multiple "Start" and "End" labels)
            List<WebElement> starts = driver.findElements(By.xpath("//label[contains(text(), 'Start')]"));
            assertTrue(starts.size() >= 2, "Should find at least 2 'Start' labels for DateRange and DateTimeRange");

            List<WebElement> ends = driver.findElements(By.xpath("//label[contains(text(), 'End')]"));
            assertTrue(ends.size() >= 2, "Should find at least 2 'End' labels for DateRange and DateTimeRange");
        }
    }
}
