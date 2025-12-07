package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for Master-Detail views.
 */
public abstract class AbstractMasterDetailTest extends BaseUITest {

    /**
     * @return the path to the Master-Detail view
     */
    protected String getPath() {
        return "tasks";
    }

    protected String getExistingItemName() {
        return "Done Task A";
    }

    @Test
    void testMasterDetailListingVisible() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName());
    }

    @Test
    void testCreateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Create").click();
        Locator field = waitForElement("xpath=(//vaadin-text-field)[2]").locator("input");
        field.fill("Created Entry");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Created Entry");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getPath() + "/1");
        Locator field = waitForElement("vaadin-text-field").locator("input");
        field.fill("");
        field.fill("Updated Entry");
        waitForAnyElementContainingText("Save").click();
        waitForAnyElementContainingText("Entry successfully saved");
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
