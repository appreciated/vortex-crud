package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for multi-form route tests.
 * Tests the MultiFormRoute functionality which displays multiple forms simultaneously
 * for a single entity.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractMultiFormRouteTest extends BaseUITest {

    /**
     * Get the path to use for multi-form tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for multi-form tests
     */
    public String getMultiFormPath() {
        return "multi-form-test";
    }

    @Test
    void testMultiFormListingVisible() {
        navigateTo(getMultiFormPath());
        WebElement webElement = waitForAnyElementContainingText("Max Mustermann");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void testMultiFormEntityLoading() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Max Mustermann").click();
        waitForUrlToBe(getMultiFormPath() + "/1");

        // Verify first form fields (Basic Information)
        waitForElementWithTagAndValue("vaadin-text-field", "Max Mustermann");
        waitForElementWithTagAndValue("vaadin-email-field", "profile@example.com");

        // Verify second form fields (Additional Details)
        waitForElementWithTagAndValue("vaadin-text-area", "This is a profile description");
        waitForElementWithTagAndValue("vaadin-integer-field", "25");
    }

    @Test
    void testMultiFormRequiredFieldValidation() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Create").click();

        // Try to save without filling required fields
        waitForAnyElementContainingText("Save").click();

        // Check for validation error message
        WebElement errorMessage = waitForAnyElementContainingText("Validation has failed for some fields");
        assertTrue(errorMessage.isDisplayed());

        // Fill required field from first form
        List<WebElement> textFields = waitForElements(By.tagName("vaadin-text-field"));
        WebElement nameField = textFields.get(0).findElement(By.tagName("input"));
        nameField.sendKeys("New Profile");

        // Fill required email field
        WebElement emailField = driver.findElement(By.tagName("vaadin-email-field"))
                .findElement(By.tagName("input"));
        emailField.sendKeys("newprofile@example.com");

        // Try to save again
        waitForAnyElementContainingText("Save").click();

        // Should navigate back to list view if validation passes
        waitForUrlToBe(getMultiFormPath());
    }

    @Test
    void testMultiFormCreateEntry() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Create").click();

        // Fill fields from first form (Basic Information)
        List<WebElement> textFields = waitForElements(By.tagName("vaadin-text-field"));
        WebElement nameField = textFields.get(0).findElement(By.tagName("input"));
        nameField.sendKeys("Created Profile");

        WebElement emailField = driver.findElement(By.tagName("vaadin-email-field"))
                .findElement(By.tagName("input"));
        emailField.sendKeys("created@example.com");

        // Fill fields from second form (Additional Details)
        WebElement descriptionField = driver.findElement(By.tagName("vaadin-text-area"))
                .findElement(By.tagName("textarea"));
        descriptionField.sendKeys("This is a created profile");

        WebElement ageField = driver.findElement(By.tagName("vaadin-integer-field"))
                .findElement(By.tagName("input"));
        ageField.sendKeys("30");

        waitForAnyElementContainingText("Save").click();

        waitForUrlToBe(getMultiFormPath());
        waitForAnyElementContainingText("Created Profile");
    }

    @Test
    void testMultiFormUpdateEntry() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Max Mustermann").click();
        waitForUrlToBe(getMultiFormPath() + "/1");

        // Update field from first form
        List<WebElement> textFields = waitForElements(By.tagName("vaadin-text-field"));
        WebElement nameField = textFields.get(0).findElement(By.tagName("input"));
        nameField.clear();
        nameField.sendKeys("Updated Profile");

        // Update field from second form
        WebElement ageField = driver.findElement(By.tagName("vaadin-integer-field"))
                .findElement(By.tagName("input"));
        ageField.clear();
        ageField.sendKeys("35");

        waitForAnyElementContainingText("Save").click();

        waitForUrlToBe(getMultiFormPath());
        waitForAnyElementContainingText("Updated Profile");
    }

    @Test
    void testMultiFormDeleteEntry() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Max Mustermann").click();
        waitForUrlToBe(getMultiFormPath() + "/1");

        waitForAnyElementContainingText("Delete").click();

        waitForUrlToBe(getMultiFormPath());
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), 'Profile Name')]"));
        assertTrue(elements.stream().noneMatch(this::isDisplayedSafe));
    }

    @Test
    void testMultiFormFieldsAcrossForms() {
        navigateTo(getMultiFormPath());
        waitForAnyElementContainingText("Create").click();

        // Fill all fields across both forms
        List<WebElement> textFields = waitForElements(By.tagName("vaadin-text-field"));
        WebElement nameField = textFields.get(0).findElement(By.tagName("input"));
        nameField.sendKeys("Complete Profile");

        WebElement emailField = driver.findElement(By.tagName("vaadin-email-field"))
                .findElement(By.tagName("input"));
        emailField.sendKeys("complete@example.com");

        WebElement descriptionField = driver.findElement(By.tagName("vaadin-text-area"))
                .findElement(By.tagName("textarea"));
        descriptionField.sendKeys("Complete profile description");

        WebElement ageField = driver.findElement(By.tagName("vaadin-integer-field"))
                .findElement(By.tagName("input"));
        ageField.sendKeys("28");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getMultiFormPath());

        // Verify all data was saved
        waitForAnyElementContainingText("Complete Profile").click();
        waitForUrlToBe(getMultiFormPath() + "/2");

        // Verify first form data
        waitForElementWithTagAndValue("vaadin-text-field", "Complete Profile");
        waitForElementWithTagAndValue("vaadin-email-field", "complete@example.com");

        // Verify second form data
        waitForElementWithTagAndValue("vaadin-text-area", "Complete profile description");
        waitForElementWithTagAndValue("vaadin-integer-field", "28");
    }
}
