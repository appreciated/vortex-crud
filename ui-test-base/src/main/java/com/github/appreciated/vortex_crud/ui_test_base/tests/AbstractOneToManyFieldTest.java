package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractOneToManyFieldTest extends BaseUITest {

    public String getPath() {
        return "one-to-many-test";
    }

    protected String getExistingParentName() {
        return "Parent A";
    }

    @Test
    void testListingVisible() {
        navigateTo(getPath());
        Locator element = waitForAnyElementContainingText(getExistingParentName());
        // it should be inside a grid cell (like in validation test)
        String tagName = (String) element.evaluate("element => element.tagName.toLowerCase()");
        assertEquals("vaadin-grid-cell-content", tagName);
    }

    @Test
    void testOpenParentAndSeeChildren() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingParentName()).click();
        waitForUrlToBe(getPath() + "/1");
        // Check that the child's names are visible on the page
        waitForAnyElementContainingText("Child A1");
        waitForAnyElementContainingText("Child A2");
    }

    @Test
    void testAddChildEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingParentName()).click();
        waitForUrlToBe(getPath() + "/1");

        Locator plusButton = waitForElement("vaadin-button:has(vaadin-icon[icon='vaadin:plus'])");
        plusButton.click();

        Locator field = waitForElement("vaadin-dialog vaadin-text-field").locator("input");
        field.fill("Created Child");

        waitForElementContainingText("vaadin-dialog//vaadin-button", "Save").click();
        waitForAnyElementContainingText("Created Child");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingParentName()).click();
        waitForUrlToBe(getPath() + "/1");
        Locator field = waitForElement("vaadin-text-field").locator("input");
        field.fill("");
        field.fill("Updated Parent");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Updated Parent");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingParentName()).click();
        waitForUrlToBe(getPath() + "/1");
        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getPath());
        List<Locator> elements = page.locator("//*[contains(text(), '" + getExistingParentName() + "')]").all();
        assertTrue(elements.stream().noneMatch(Locator::isVisible));
    }
}
