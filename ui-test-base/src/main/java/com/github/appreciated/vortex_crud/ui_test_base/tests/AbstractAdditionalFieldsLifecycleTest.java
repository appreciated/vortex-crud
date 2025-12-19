package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractAdditionalFieldsLifecycleTest extends BaseUITest {

    public String getAdditionalFieldsPath() {
        return "additional-fields-test";
    }

    @Test
    void testListingVisible() {
        navigateTo(getAdditionalFieldsPath());
        Locator webElement = waitForAnyElementContainingText("Test Entity");
        String tagName = (String) webElement.evaluate("element => element.tagName.toLowerCase()");
        assertEquals("vaadin-grid-cell-content", tagName);
    }

    @Test
    void testEntityLoading() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        // This test only verifies that navigation to detail view works
        // Specific field checks are in their respective tests
        assertThat(page.locator("vaadin-form-layout")).isVisible();
    }

    @Test
    void testCreateEntry() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        Locator nameField = waitForElementContainingText("vaadin-text-field", "Name").locator("input");
        nameField.fill("Created Entity");

        Locator passwordField = waitForElement("vaadin-password-field").locator("input");
        passwordField.fill("newpassword");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Created Entity");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        Locator nameField = waitForElementContainingText("vaadin-text-field", "Name").locator("input");
        nameField.fill("");
        nameField.fill("Updated Entity");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Updated Entity");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getAdditionalFieldsPath());

        // Wait for the grid to refresh and the entity to disappear
        waitForTextToDisappear("Test Entity");
        assertTrue(page.getByText("Test Entity").all().isEmpty());
    }
}
