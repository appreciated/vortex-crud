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
    public String getValidationPath() {
        return "field-validation-test";
    }

   @Test
    void testValidationListingVisible() {
        navigateTo(getValidationPath());
        WebElement webElement = waitForAnyElementContainingText("Test Value");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void testValidationEntityLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-text-field", "Test Value");
        waitForElementWithTagAndValue("vaadin-email-field", "test@example.com");
        waitForElementWithTagAndValue("vaadin-big-decimal-field", "42");
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-01-01");
        waitForElementWithTagAndValue("vaadin-select-item", "Option1");
    }

   @Test
    void testRequiredFieldValidation() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Erstellen").click();
        
        // Try to save without filling required fields
        waitForAnyElementContainingText("Speichern").click();
        
        // Check for validation error message - look for the specific constraint violation message
        WebElement errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertTrue(errorMessage.isDisplayed());
        
        // Find the required field by label text instead of required attribute
        WebElement requiredField = waitForElementContainingText("vaadin-text-field", "Required")
                .findElement(By.tagName("input"));
        requiredField.sendKeys("New Test Value");
        
        // Fill email field (second text field based on HTML)
        WebElement emailField = driver.findElement(By.tagName("vaadin-email-field"))
                .findElement(By.tagName("input"));
        emailField.sendKeys("new@example.com");
        
        // Fill numeric field with Double value to avoid ClassCastException
        WebElement numericField = driver.findElement(By.tagName("vaadin-big-decimal-field"))
                .findElement(By.tagName("input"));
        numericField.sendKeys("50.0");
        
        // Try to save again
        waitForAnyElementContainingText("Speichern").click();
        
        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }

     @Test
    void testEmailValidation() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Erstellen").click();
        
        // Fill required field by finding the field with "Required" label
        WebElement requiredField = waitForElementContainingText("vaadin-text-field", "Required")
                .findElement(By.tagName("input"));
        requiredField.sendKeys("Email Test");
        
        // Fill email field with invalid value (second text field based on HTML)
        WebElement emailField = driver.findElement(By.tagName("vaadin-email-field"))
                .findElement(By.tagName("input"));
        emailField.sendKeys("invalid-email");
        
        // Fill numeric field with valid Double value
        WebElement numericField = driver.findElement(By.tagName("vaadin-big-decimal-field"))
                .findElement(By.tagName("input"));
        numericField.sendKeys("50.0");
        
        // Try to save
        waitForAnyElementContainingText("Speichern").click();
        
        // Check for validation error message
        WebElement errorMessage = waitForAnyElementContainingText("valid email");
        assertTrue(errorMessage.isDisplayed());
        
        // Correct the email
        emailField.clear();
        emailField.sendKeys("valid@example.com");
        
        // Try to save again
        waitForAnyElementContainingText("Speichern").click();
        
        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }

     @Test
    void testNumericValidation() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Erstellen").click();
        
        // Fill required field
        WebElement requiredField = waitForElementContainingText("vaadin-text-field", "Required")
                .findElement(By.tagName("input"));
        requiredField.sendKeys("Numeric Test");
        
        // Fill email field (second text field)
        WebElement emailField = driver.findElement(By.tagName("vaadin-email-field"))
                .findElement(By.tagName("input"));
        emailField.sendKeys("numeric@example.com");
        
        // Fill numeric field with invalid value (negative number)
        WebElement numericField = driver.findElement(By.tagName("vaadin-big-decimal-field"))
                .findElement(By.tagName("input"));
        numericField.sendKeys("-5.0");
        
        // Try to save
        waitForAnyElementContainingText("Speichern").click();
        
        // Check for validation error message
        WebElement errorMessage = waitForAnyElementContainingText("greater than 0");
        assertTrue(errorMessage.isDisplayed());
        
        // Correct the numeric value
        numericField.clear();
        numericField.sendKeys("25.0");
        
        // Try to save again
        waitForAnyElementContainingText("Speichern").click();
        
        // Should navigate back to list view if validation passes
        waitForUrlToBe(getValidationPath());
    }
}