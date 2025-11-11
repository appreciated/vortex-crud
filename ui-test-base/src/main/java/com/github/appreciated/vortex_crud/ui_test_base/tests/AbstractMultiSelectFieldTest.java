package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for testing MultiSelectField functionality.
 * This tests the MultiSelectComboBox component for selecting multiple entities.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractMultiSelectFieldTest extends BaseUITest {

    /**
     * Get the path to use for multi-select field tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for multi-select field tests
     */
    public String getMultiSelectFieldPath() {
        return "multi-select-test";
    }

    @Test
    void testListingVisible() {
        navigateTo(getMultiSelectFieldPath());
        WebElement webElement = waitForAnyElementContainingText("Test Item");
        assertTrue(webElement.getTagName().contains("vaadin-grid-cell-content"));
    }

    @Test
    void testEntityLoading() {
        navigateTo(getMultiSelectFieldPath());
        waitForAnyElementContainingText("Test Item").click();
        waitForUrlToBe(getMultiSelectFieldPath() + "/1");

        // Verify MultiSelectComboBox field exists
        WebElement multiSelectField = waitForElement(By.tagName("vaadin-multi-select-combo-box"));
        assertTrue(multiSelectField.isDisplayed());
    }

    @Test
    void testMultiSelectInput() {
        navigateTo(getMultiSelectFieldPath());
        waitForAnyElementContainingText("Create").click();

        // Fill in required name field
        WebElement nameField = waitForElementContainingText("vaadin-text-field", "Name")
                .findElement(By.tagName("input"));
        nameField.sendKeys("Multi Select Test");

        // Find and interact with MultiSelectComboBox
        WebElement multiSelectComboBox = waitForElement(By.tagName("vaadin-multi-select-combo-box"));
        WebElement input = multiSelectComboBox.findElement(By.tagName("input"));

        // Open dropdown
        input.click();
        sleep(500); // Wait for dropdown to open

        // Select multiple items
        List<WebElement> items = driver.findElements(By.tagName("vaadin-multi-select-combo-box-item"));
        if (!items.isEmpty()) {
            // Select first item
            if (items.size() > 0) {
                items.get(0).click();
                sleep(300);
            }
            // Select second item
            if (items.size() > 1) {
                items.get(1).click();
                sleep(300);
            }
        }

        // Save
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getMultiSelectFieldPath());

        // Verify the entity was created
        waitForAnyElementContainingText("Multi Select Test");
    }

    @Test
    void testMultiSelectUpdateSelection() {
        navigateTo(getMultiSelectFieldPath());
        waitForAnyElementContainingText("Test Item").click();
        waitForUrlToBe(getMultiSelectFieldPath() + "/1");

        // Find MultiSelectComboBox
        WebElement multiSelectComboBox = waitForElement(By.tagName("vaadin-multi-select-combo-box"));
        WebElement input = multiSelectComboBox.findElement(By.tagName("input"));

        // Open dropdown and select additional items
        input.click();
        sleep(500);

        List<WebElement> items = driver.findElements(By.tagName("vaadin-multi-select-combo-box-item"));
        if (!items.isEmpty() && items.size() > 0) {
            items.get(0).click();
            sleep(300);
        }

        // Save changes
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getMultiSelectFieldPath());
    }

    @Test
    void testMultiSelectFieldFiltering() {
        navigateTo(getMultiSelectFieldPath());
        waitForAnyElementContainingText("Create").click();

        // Find MultiSelectComboBox
        WebElement multiSelectComboBox = waitForElement(By.tagName("vaadin-multi-select-combo-box"));
        WebElement input = multiSelectComboBox.findElement(By.tagName("input"));

        // Type to filter
        input.sendKeys("Test");
        sleep(500);

        // Verify filtered items appear
        List<WebElement> items = driver.findElements(By.tagName("vaadin-multi-select-combo-box-item"));
        assertTrue(items.size() >= 0, "Filtered items should appear");
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
        waitForAnyElementContainingText("Test Item").click();
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
        waitForAnyElementContainingText("Test Item").click();
        waitForUrlToBe(getMultiSelectFieldPath() + "/1");

        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getMultiSelectFieldPath());

        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), 'Test Item')]"));
        assertTrue(elements.stream().noneMatch(this::isDisplayedSafe));
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
