package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for IdField.
 */
public abstract class AbstractIdFieldTest extends BaseUITest {

    protected String getPath() {
        return "projects-list";
    }

    protected String getItemName() {
        return "Project 1";
    }

    protected String getExpectedId() {
        return "1";
    }

    @Test
    void testIdFieldIsReadOnly() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getItemName()).click();

        // Wait for form to load
        waitForElement("vaadin-form-layout");

        // Find the ID field by its label
        Locator idField = page.locator("vaadin-text-field").filter(new Locator.FilterOptions().setHasText("!{ID}!")).locator("vaadin-input-container");

        // Ensure ID field exists
        assertThat(idField).isVisible();

        // Ensure it is read-only
        Boolean isReadOnly = (Boolean) idField.evaluate("element => element.readonly");
        assertTrue(isReadOnly, "ID field should be read-only");

        // Also verify the value matches expected ID
        assertThat(idField).hasValue(getExpectedId());
    }
}
