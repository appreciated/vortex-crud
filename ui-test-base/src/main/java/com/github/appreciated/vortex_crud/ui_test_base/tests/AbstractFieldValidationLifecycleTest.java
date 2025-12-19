package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractFieldValidationLifecycleTest extends BaseUITest {

    public String getValidationPath() {
        return "field-validation-test";
    }

    @Test
    void testValidationListingVisible() {
        navigateTo(getValidationPath());
        Locator webElement = waitForAnyElementContainingText("Test Value");
        String tagName = (String) webElement.evaluate("element => element.tagName.toLowerCase()");
        assertEquals("vaadin-grid-cell-content", tagName);
    }

    @Test
    void testCreateEntry() {
        navigateTo(getValidationPath());
        waitForButton("Create").click();

        Locator requiredField = waitForElementContainingText("vaadin-text-field", "Required").locator("input");
        requiredField.fill("Created Value");

        // Fill other potentially required fields to ensure save works (generic happy path)
        page.locator("vaadin-email-field input").first().fill("created@example.com");
        page.locator("vaadin-number-field input").first().fill("100.0");

        waitForButton("Save").click();

        waitForUrlToBe(getValidationPath());
        waitForAnyElementContainingText("Created Value");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");

        Locator requiredField = waitForElementContainingText("vaadin-text-field", "Required").locator("input");
        requiredField.fill("Updated Value");

        waitForButton("Save").click();

        waitForUrlToBe(getValidationPath());
        waitForAnyElementContainingText("Updated Value");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");

        waitForButton("Delete").click();

        waitForUrlToBe(getValidationPath());

        // Wait for the grid to refresh and the entity to disappear
        waitForTextToDisappear("Test Value");
        List<Locator> elements = page.locator("//*[contains(text(), 'Test Value')]").all();
        assertTrue(elements.stream().noneMatch(Locator::isVisible));
    }
}
