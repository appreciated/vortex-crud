package com.github.appreciated.vortex_crud.ui_test_base.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the slide-in form dialog variant.
 * Inherits the CRUD behaviour tests from {@link AbstractCardTest} and
 * adds an assertion that the dialog uses the slide styling.
 */
public abstract class AbstractFormSlideTest extends AbstractCardTest {

    @Override
    protected String getPath() {
        return "images-slide";
    }

    @Test
    void testDialogHasSlideClass() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Create").click();
        WebElement dialog = waitForElement(By.cssSelector("vaadin-dialog.form-slide-dialog"));
        assertTrue(dialog.isDisplayed());
    }
}
