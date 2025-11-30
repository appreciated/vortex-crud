package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Base test for single form routes.
 */
public abstract class AbstractSingleFormRouteTest extends BaseUITest {

    protected String getSingleFormPath() {
        return "single-form-test";
    }

    @Test
    void testSingleFormRoute() {
        navigateTo(getSingleFormPath());
        // Should load entity with ID 1
        waitForAnyElementContainingText("Test Entity 1");
        // Should allow editing
        WebElement nameField = waitForElementContainingText("vaadin-text-field", "Name")
                .findElement(By.tagName("input"));
        nameField.clear();
        nameField.sendKeys("Updated via Single Form");
        waitForAnyElementContainingText("Save").click();

        // Reload to verify? Or just check if notification/success.
        // Since SingleFormRoute stays on the same page, we can reload.
        navigateTo(getSingleFormPath());
        waitForAnyElementContainingText("Updated via Single Form");
    }
}
