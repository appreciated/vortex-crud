package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple tests for a slide-in form dialog that uses a {@code CardFactory}.
 * <p>
 * The tests are intentionally lightweight and inspired by
 * {@link AbstractFieldValidationTest} while focusing on the slide variant
 * of the form dialog.
 */
public abstract class AbstractFormSlideTest extends BaseUITest {

    /**
     * Path to the slide-in form demo.
     *
     * @return the URL path used for the test
     */
    protected String getPath() {
        return "images";
    }

    @Test
    void testListingVisible() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Red");
    }

    @Test
    void testRequiredFieldValidation() {
        navigateTo(getPath());
        waitForButton("Create").click();

        // Attempt to save without filling required fields
        waitForButton("Save").click();

        Locator errorMessage = waitForAnyElementContainingText("Entry could not be saved");
        assertTrue(errorMessage.isVisible());

        // Fill required title field and save
        Locator titleField = waitForElement("//vaadin-dialog//vaadin-text-field").locator("input");
        titleField.fill("New Image");

        waitForButton("Save").click();

        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("New Image");
    }
}
