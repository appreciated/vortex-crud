package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
        waitForAnyElementContainingText("Create").click();

        // Attempt to save without filling required fields
        waitForAnyElementContainingText("Save").click();

        WebElement errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertTrue(errorMessage.isDisplayed());

        // Fill required title field and save
        WebElement titleField = waitForElement(By.xpath("//vaadin-dialog-overlay//vaadin-text-field"))
                .findElement(By.tagName("input"));
        titleField.sendKeys("New Image");

        waitForAnyElementContainingText("Save").click();

        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("New Image");
    }
}

