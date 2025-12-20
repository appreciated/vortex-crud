package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractLifecycleTest extends BaseUITest {

    public String getAdditionalFieldsPath() {
        return "lifecycle-test";
    }

    /**
     * Override this method to provide the expected entity name from test data.
     * Default is "Lifecycle Test Entity".
     */
    protected String getExpectedEntityName() {
        return "Lifecycle Test Entity";
    }

    @Test
    void testListingVisible() {
        navigateTo(getAdditionalFieldsPath());
        Locator webElement = page.getByText(getExpectedEntityName(), new Page.GetByTextOptions().setExact(true)).first();
        webElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String tagName = (String) webElement.evaluate("element => element.tagName.toLowerCase()");
        assertEquals("vaadin-grid-cell-content", tagName);
    }

    @Test
    void testEntityLoading() {
        navigateTo(getAdditionalFieldsPath());
        page.getByText(getExpectedEntityName(), new Page.GetByTextOptions().setExact(true)).first().click();
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

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Created Entity");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getAdditionalFieldsPath());
        page.getByText(getExpectedEntityName(), new Page.GetByTextOptions().setExact(true)).first().click();
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
        page.getByText(getExpectedEntityName(), new Page.GetByTextOptions().setExact(true)).first().click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getAdditionalFieldsPath());

        // Wait for the grid to refresh and the entity to disappear
        waitForTextToDisappear(getExpectedEntityName());
        assertTrue(page.getByText(getExpectedEntityName(), new Page.GetByTextOptions().setExact(true)).all().isEmpty());
    }
}
