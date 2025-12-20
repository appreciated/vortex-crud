package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
        page.getByText(getItemName(), new Page.GetByTextOptions().setExact(true)).first().click();

        // Wait for form to load
        waitForElement("vaadin-form-layout");

        // Find the ID field by its label
        Locator idField = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("ID"));

        // Ensure ID field exists
        assertThat(idField).isVisible();

        // Ensure it is read-only

        assertThat(idField).hasAttribute("readonly", "");

        // Also verify the value matches expected ID
        assertThat(idField).hasValue(getExpectedId());
    }
}
