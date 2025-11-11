package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for testing MultiSelectField functionality.
 * This tests the MultiSelectComboBox component for selecting multiple entities.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractMultiSelectFieldTest extends BaseUITest {

    public String getMultiSelectFieldPath() {
        return "multi-select-test";
    }

    protected String getExistingItemName() {
        return "Test Item";
    }

    @Test
    void testListingVisible() {
        navigateTo(getMultiSelectFieldPath());
        WebElement element = waitForAnyElementContainingText(getExistingItemName());
        assertEquals("vaadin-grid-cell-content", element.getTagName());
    }

    @Test
    void testEntityLoading() {
        navigateTo(getMultiSelectFieldPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getMultiSelectFieldPath() + "/1");

        // Verify MultiSelectComboBox field exists
        WebElement multiSelectField = waitForElement(By.tagName("vaadin-multi-select-combo-box"));
        assertTrue(multiSelectField.isDisplayed());
    }

    @Test
    void testCreateEntry() {
        navigateTo(getMultiSelectFieldPath());
        waitForAnyElementContainingText("Create").click();

        WebElement nameField = waitForElementContainingText("vaadin-text-field", "Name")
                .findElement(By.tagName("input"));
        nameField.sendKeys("Created Entry");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getMultiSelectFieldPath());
        waitForAnyElementContainingText("Created Entry");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getMultiSelectFieldPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getMultiSelectFieldPath() + "/1");

        WebElement nameField = waitForElementContainingText("vaadin-text-field", "Name")
                .findElement(By.tagName("input"));
        nameField.clear();
        nameField.sendKeys("Updated Entry");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getMultiSelectFieldPath());
        waitForAnyElementContainingText("Updated Entry");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getMultiSelectFieldPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getMultiSelectFieldPath() + "/1");

        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getMultiSelectFieldPath());

        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + getExistingItemName() + "')]"));
        assertTrue(elements.stream().noneMatch(this::isDisplayedSafe));
    }
}
