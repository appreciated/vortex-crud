package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractManyToManyFieldTest extends BaseUITest {

    public String getPath() {
        return "many-to-many-test";
    }

    protected String getExistingItemName() {
        return "Item 1";
    }

    @Test
    void testListingVisible() {
        navigateTo(getPath());
        Locator element = waitForAnyElementContainingText(getExistingItemName());
        String tagName = (String) element.evaluate("el => el.tagName.toLowerCase()");
        assertEquals("vaadin-grid-cell-content", tagName);
    }

    @Test
    void testOpenItemAndSeeRelated() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getPath() + "/1");
        // Related entries seeded: Item 2, Item 3
        waitForAnyElementContainingText("Item 2");
        waitForAnyElementContainingText("Item 3");
    }

    @Test
    void testAddEntryToChildCollection() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Item 2").click();
        waitForUrlToBe(getPath() + "/2");
        List<Locator> elements = page.locator("//*[contains(text(), 'Item 3')]").all();
        assertTrue(elements.stream().noneMatch(this::isDisplayedSafe));
        waitForElement("//vaadin-icon[@icon='vaadin:plus']/..").click();
        waitForAnyElementContainingText("Item 3").click();
        waitForAnyElementContainingText("Link").click();
        waitForAnyElementContainingText("Item 3");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getPath() + "/1");
        Locator field = waitForElement("vaadin-text-field").locator("input");
        field.fill("");
        field.fill("Updated Item");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Updated Item");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getPath() + "/1");
        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getPath());
        List<Locator> elements = page.locator("//*[contains(text(), '" + getExistingItemName() + "')]").all();
        assertTrue(elements.stream().noneMatch(this::isDisplayedSafe));
    }
}
