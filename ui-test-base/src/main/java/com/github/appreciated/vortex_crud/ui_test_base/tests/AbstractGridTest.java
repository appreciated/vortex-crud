package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for grid based project listings. Concrete implementations
 * must provide the route path and expected values.
 */
public abstract class AbstractGridTest extends BaseUITest {

    protected String getPath() {
        return "images-grid";
    }

    protected String getExpectedVisibleValue() {
        return "Red";
    }

    protected String getFilterValuePresent() {
        return "Red";
    }

    protected String getFilterValueAbsent() {
        return "Green";
    }

    /**
     * @return id of the entity expected when opening details; defaults to "1"
     */
    protected String getDetailId() {
        return "1";
    }

    @Test
    void testGridListingVisible() {
        navigateTo(getPath());
        Locator element = waitForAnyElementContainingText(getExpectedVisibleValue());
        String tagName = (String) element.evaluate("el => el.tagName.toLowerCase()");
        assertEquals("h4", tagName);
    }

    @Test
    void testNavigateToDetail() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue()).click();
        waitForUrlToBe(getPath() + "/" + getDetailId());
    }

    @Test
    void testFilterIfAvailable() {
        String present = getFilterValuePresent();
        String absent = getFilterValueAbsent();
        if (present == null || absent == null) {
            return; // no filter configured
        }
        navigateTo(getPath());
        Locator filter = waitForElement("vaadin-text-field").locator("input");
        filter.fill(present);
        waitForElementWithTagAndValue("vaadin-text-field", present);
        page.waitForTimeout(500);

        List<Locator> hidden = page.locator("//*[contains(text(), '" + absent + "')]").all()
                .stream()
                .filter(this::isDisplayedSafe)
                .toList();
        assertEquals(0, hidden.size());

        for (int i = 0; i < present.length(); i++) {
            filter.press("Backspace");
        }
        page.waitForTimeout(500);
        waitForAnyElementContainingText(absent);
    }

    @Test
    void testImagesDisplayed() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue());
        Locator img = waitForElement("img");
        assertTrue(img.isVisible());
    }

    @Test
    void testCreateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Create").click();
        Locator field = waitForElement("//vaadin-dialog//vaadin-text-field").locator("input");
        field.fill("Created Entry");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Created Entry");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue()).click();
        waitForUrlToBe(getPath() + "/" + getDetailId());
        Locator field = waitForElement("vaadin-text-field").locator("input");
        field.fill("");
        field.fill("Updated Entry");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Updated Entry");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue()).click();
        waitForUrlToBe(getPath() + "/" + getDetailId());
        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getPath());
        List<Locator> elements = page.locator("//*[contains(text(), '" + getExpectedVisibleValue() + "')]").all();
        assertTrue(elements.stream().noneMatch(this::isDisplayedSafe));
    }
}
