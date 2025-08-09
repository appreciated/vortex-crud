package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for form elements tests.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractFormElementsTest extends BaseUITest {

    /**
     * Get the path to use for form elements tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for form elements tests
     */
    private String getFormElementsPath() {
        return "form-elements-test";
    }

    @Test
    void testFormElementsListingVisible() {
        navigateTo(getFormElementsPath());
        WebElement webElement = waitForAnyElementContainingText("Sample Text");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void testFormElementsNavigationPossible() {
        navigateTo(getFormElementsPath());
        waitForAnyElementContainingText("Sample Text").click();
        waitForUrlToBe(getFormElementsPath() + "/1");
        
        // Verify text field
        waitForElementWithTagAndValue("vaadin-text-field", "Sample Text");
        
        // Verify text area
        waitForElementWithTagAndValue("vaadin-text-area", "This is a longer text that would go in a text area field.");
        
        // Verify number field
        waitForElementWithTagAndValue("vaadin-number-field", "42");
        
        // Verify date field
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-05-15");
        
        // Verify time field
        waitForElementWithTagAndValue("vaadin-time-picker", "14:30");
        
        // Verify checkbox
        WebElement checkbox = driver.findElement(By.cssSelector("vaadin-checkbox[checked]"));
        assertTrue(checkbox.isDisplayed());
        
        // Verify select field
        waitForElementWithTagAndValue("vaadin-select-item", "OPTION1");
        
        // Verify email field
        waitForElementWithTagAndValue("vaadin-email-field", "test@example.com");
        
        // Verify URL field
        waitForElementWithTagAndValue("vaadin-text-field", "https://example.com");
    }

    @Test
    void testFormElementsEditing() {
        navigateTo(getFormElementsPath());
        waitForAnyElementContainingText("Sample Text").click();
        waitForUrlToBe(getFormElementsPath() + "/1");
        
        // Edit text field
        WebElement textField = waitForElementWithTagAndValue("vaadin-text-field", "Sample Text");
        textField.clear();
        textField.sendKeys("Updated Text");
        
        // Edit text area
        WebElement textArea = waitForElementWithTagAndValue("vaadin-text-area", "This is a longer text that would go in a text area field.");
        textArea.clear();
        textArea.sendKeys("This is an updated text area content.");
        
        // Edit number field
        WebElement numberField = waitForElementWithTagAndValue("vaadin-number-field", "42");
        numberField.clear();
        numberField.sendKeys("99");
        
        // Click save button
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the list view
        waitForUrlToBe(getFormElementsPath());
        
        // Navigate back to the edited item
        waitForAnyElementContainingText("Updated Text").click();
        waitForUrlToBe(getFormElementsPath() + "/1");
        
        // Verify the values were updated
        waitForElementWithTagAndValue("vaadin-text-field", "Updated Text");
        waitForElementWithTagAndValue("vaadin-text-area", "This is an updated text area content.");
        waitForElementWithTagAndValue("vaadin-number-field", "99");
    }

    @Test
    void testFormElementsCreation() {
        navigateTo(getFormElementsPath() + "/new");
        
        // Fill text field
        WebElement textField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        textField.sendKeys("New Form Element");
        
        // Fill text area
        WebElement textArea = driver.findElement(By.cssSelector("vaadin-text-area"));
        textArea.sendKeys("This is a new text area content.");
        
        // Fill number field
        WebElement numberField = driver.findElement(By.cssSelector("vaadin-number-field"));
        numberField.sendKeys("123");
        
        // Fill date field
        WebElement dateField = driver.findElement(By.cssSelector("vaadin-date-picker"));
        dateField.sendKeys("2023-08-20");
        
        // Fill time field
        WebElement timeField = driver.findElement(By.cssSelector("vaadin-time-picker"));
        timeField.sendKeys("10:30");
        
        // Check checkbox
        WebElement checkbox = driver.findElement(By.cssSelector("vaadin-checkbox"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        
        // Fill email field
        WebElement emailField = driver.findElement(By.cssSelector("vaadin-email-field"));
        emailField.sendKeys("new@example.com");
        
        // Fill URL field
        WebElement urlField = driver.findElements(By.cssSelector("vaadin-text-field")).get(1);
        urlField.sendKeys("https://newexample.com");
        
        // Click save button
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the list view
        waitForUrlToBe(getFormElementsPath());
        
        // Verify the new item appears in the list
        WebElement newItem = waitForAnyElementContainingText("New Form Element");
        assertTrue(newItem.isDisplayed());
    }
}