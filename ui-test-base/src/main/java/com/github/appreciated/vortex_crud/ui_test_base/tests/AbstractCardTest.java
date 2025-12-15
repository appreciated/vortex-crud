package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for card based project listings.
 */
public abstract class AbstractCardTest extends BaseUITest {

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

    protected String getDetailId() {
        return "1";
    }

    @Test
    void testCardListingVisible() {
        navigateTo(getPath());
        Locator element = waitForAnyElementContainingText(getExpectedVisibleValue());
        assertThat(element).isVisible();
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
            return;
        }
        navigateTo(getPath());
        Locator filter = waitForElement("vaadin-text-field").locator("input");
        filter.fill(present);
        filter.press("Enter");
        page.waitForTimeout(500);
        waitForAnyElementContainingText(present);
        List<Locator> hidden = page.locator("//*[contains(text(), '" + absent + "')]").all()
                .stream()
                .filter(Locator::isVisible)
                .toList();
        assertTrue(hidden.isEmpty());
        filter.press("Backspace");
        filter.press("Backspace");
        filter.press("Backspace");
        waitForAnyElementContainingText(absent);
    }

    @Test
    void testCreateEntry() {
        navigateTo(getPath());
        waitForButton("Create").click();
        Locator field = waitForElement("//vaadin-dialog//vaadin-text-field").locator("input");
        field.fill("Created Entry");
        waitForButton("Save").click();
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
        waitForButton("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Updated Entry");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue()).click();
        waitForUrlToBe(getPath() + "/" + getDetailId());
        waitForButton("Delete").click();
        waitForUrlToBe(getPath());
        List<Locator> elements = page.locator("//*[contains(text(), '" + getExpectedVisibleValue() + "')]").all();
        elements.forEach(locator -> locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)));
    }

    @Test
    void testImagesDisplayed() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue());
        Locator img = waitForElement("img");
        assertThat(img).isVisible();
    }
}
