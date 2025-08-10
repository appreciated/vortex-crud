package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for field validation tests.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractFieldValidationTest extends BaseUITest {

    /**
     * Get the path to use for validation tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for validation tests
     */
    private String getValidationPath() {
        return "field-validation-test";
    }

    @Test
    void testValidationListingVisible() {
        navigateTo(getValidationPath());
        WebElement webElement = waitForAnyElementContainingText("Test Value");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void testValidationNavigationPossible() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-text-field", "Test Value");
        waitForElementWithTagAndValue("vaadin-text-field", "test@example.com");
        waitForElementWithTagAndValue("vaadin-number-field", "42");
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-01-01");
        waitForElementWithTagAndValue("vaadin-select-item", "OPTION1");
    }

    @Test
    void testRequiredFieldValidation() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Erstellen").click();
        
        // Try to save without filling required fields
        waitForAnyElementContainingText("Speichern").click();
        
        // Check for validation error message
        WebElement errorMessage = waitForAnyElementContainingText("required");
        assertTrue(errorMessage.isDisplayed());
        
        // Fill required field
        WebElement requiredField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        requiredField.sendKeys("New Test Value");
        
        // Fill other fields with valid values
        WebElement emailField = driver.findElement(By.cssSelector("vaadin-email-field"));
        emailField.sendKeys("new@example.com");
        
        WebElement numericField = driver.findElement(By.cssSelector("vaadin-number-field"));
        numericField.sendKeys("50");
        
        // Try to save again
        waitForAnyElementContainingText("Save").click();
        
        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }

    @Test
    void testEmailValidation() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Erstellen").click();
        
        // Fill required field
        WebElement requiredField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        requiredField.sendKeys("Email Test");
        
        // Fill email field with invalid value
        WebElement emailField = driver.findElement(By.cssSelector("vaadin-email-field"));
        emailField.sendKeys("invalid-email");
        
        // Fill other fields with valid values
        WebElement numericField = driver.findElement(By.cssSelector("vaadin-number-field"));
        numericField.sendKeys("50");
        
        // Try to save
        waitForAnyElementContainingText("Save").click();
        
        // Check for validation error message
        WebElement errorMessage = waitForAnyElementContainingText("valid email");
        assertTrue(errorMessage.isDisplayed());
        
        // Correct the email
        emailField.clear();
        emailField.sendKeys("valid@example.com");
        
        // Try to save again
        waitForAnyElementContainingText("Save").click();
        
        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }

    @Test
    void testNumericValidation() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Erstellen").click();
        
        // Fill required field
        WebElement requiredField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        requiredField.sendKeys("Numeric Test");
        
        // Fill email field
        WebElement emailField = driver.findElement(By.cssSelector("vaadin-email-field"));
        emailField.sendKeys("numeric@example.com");
        
        // Fill numeric field with invalid value
        WebElement numericField = driver.findElement(By.cssSelector("vaadin-number-field"));
        numericField.sendKeys("-5");
        
        // Try to save
        waitForAnyElementContainingText("Save").click();
        
        // Check for validation error message
        WebElement errorMessage = waitForAnyElementContainingText("greater than 0");
        assertTrue(errorMessage.isDisplayed());
        
        // Correct the numeric value
        numericField.clear();
        numericField.sendKeys("25");
        
        // Try to save again
        waitForAnyElementContainingText("Save").click();
        
        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }
}