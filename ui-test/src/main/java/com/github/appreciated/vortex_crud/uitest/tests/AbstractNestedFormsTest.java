package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for nested forms tests.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractNestedFormsTest extends BaseUITest {

    /**
     * Get the path to use for orders tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for orders tests
     */
    private String getOrdersPath() {
        return "orders-test";
    }

    @Test
    void testOrdersListingVisible() {
        navigateTo(getOrdersPath());
        WebElement webElement = waitForAnyElementContainingText("John Smith");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void testOrderNavigationPossible() {
        navigateTo(getOrdersPath());
        waitForAnyElementContainingText("John Smith").click();
        waitForUrlToBe(getOrdersPath() + "/1");
        
        // Verify order details
        waitForElementWithTagAndValue("vaadin-text-field", "John Smith");
        waitForElementWithTagAndValue("vaadin-email-field", "john@example.com");
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-07-10");
        waitForElementWithTagAndValue("vaadin-text-area", "123 Main St, Apt 4B, Anytown, ST 12345");
        waitForElementWithTagAndValue("vaadin-select-item", "PROCESSING");
        
        // Verify order items are displayed
        waitForAnyElementContainingText("Smartphone Case");
        waitForAnyElementContainingText("Wireless Headphones");
        waitForAnyElementContainingText("Screen Protector");
        
        // Verify order notes are displayed
        waitForAnyElementContainingText("Customer requested express shipping");
        waitForAnyElementContainingText("Package prepared for shipping");
    }

    @Test
    void testOrderItemsNavigation() {
        navigateTo(getOrdersPath());
        waitForAnyElementContainingText("John Smith").click();
        waitForUrlToBe(getOrdersPath() + "/1");
        
        // Click on an order item
        waitForAnyElementContainingText("Smartphone Case").click();
        
        // Verify order item details
        waitForElementWithTagAndValue("vaadin-text-field", "Smartphone Case");
        waitForElementWithTagAndValue("vaadin-number-field", "1");
        waitForElementWithTagAndValue("vaadin-number-field", "29.99");
        
        // Go back to order
        waitForAnyElementContainingText("Back").click();
        waitForUrlToBe(getOrdersPath() + "/1");
    }

    @Test
    void testAddingOrderItem() {
        navigateTo(getOrdersPath());
        waitForAnyElementContainingText("John Smith").click();
        waitForUrlToBe(getOrdersPath() + "/1");
        
        // Click add item button
        waitForAnyElementContainingText("Add Item").click();
        
        // Fill in new item details
        WebElement productNameField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        productNameField.sendKeys("New Product");
        
        WebElement quantityField = driver.findElement(By.cssSelector("vaadin-number-field"));
        quantityField.sendKeys("2");
        
        WebElement priceField = driver.findElements(By.cssSelector("vaadin-number-field")).get(1);
        priceField.sendKeys("19.99");
        
        // Save the new item
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the order view
        waitForUrlToBe(getOrdersPath() + "/1");
        
        // Verify the new item appears
        WebElement newItem = waitForAnyElementContainingText("New Product");
        assertTrue(newItem.isDisplayed());
    }

    @Test
    void testAddingOrderNote() {
        navigateTo(getOrdersPath());
        waitForAnyElementContainingText("John Smith").click();
        waitForUrlToBe(getOrdersPath() + "/1");
        
        // Click add note button
        waitForAnyElementContainingText("Add Note").click();
        
        // Fill in new note details
        WebElement noteTextField = driver.findElement(By.cssSelector("vaadin-text-area[required]"));
        noteTextField.sendKeys("This is a new test note");
        
        // Save the new note
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the order view
        waitForUrlToBe(getOrdersPath() + "/1");
        
        // Verify the new note appears
        WebElement newNote = waitForAnyElementContainingText("This is a new test note");
        assertTrue(newNote.isDisplayed());
    }

    @Test
    void testOrderCreationWithNestedItems() {
        navigateTo(getOrdersPath() + "/new");
        
        // Fill order details
        WebElement customerNameField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        customerNameField.sendKeys("New Customer");
        
        WebElement emailField = driver.findElement(By.cssSelector("vaadin-email-field"));
        emailField.sendKeys("newcustomer@example.com");
        
        WebElement shippingAddressField = driver.findElement(By.cssSelector("vaadin-text-area"));
        shippingAddressField.sendKeys("789 New Street, Newtown, ST 54321");
        
        WebElement billingAddressField = driver.findElements(By.cssSelector("vaadin-text-area")).get(1);
        billingAddressField.sendKeys("789 New Street, Newtown, ST 54321");
        
        // Add an order item
        waitForAnyElementContainingText("Add Item").click();
        
        // Fill in item details
        WebElement productNameField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        productNameField.sendKeys("First New Product");
        
        WebElement quantityField = driver.findElement(By.cssSelector("vaadin-number-field"));
        quantityField.sendKeys("1");
        
        WebElement priceField = driver.findElements(By.cssSelector("vaadin-number-field")).get(1);
        priceField.sendKeys("29.99");
        
        // Save the item
        waitForAnyElementContainingText("Save").click();
        
        // Add a note
        waitForAnyElementContainingText("Add Note").click();
        
        // Fill in note details
        WebElement noteTextField = driver.findElement(By.cssSelector("vaadin-text-area[required]"));
        noteTextField.sendKeys("Initial order note");
        
        // Save the note
        waitForAnyElementContainingText("Save").click();
        
        // Save the entire order
        waitForAnyElementContainingText("Save Order").click();
        
        // Verify we're back at the orders list
        waitForUrlToBe(getOrdersPath());
        
        // Verify the new order appears
        WebElement newOrder = waitForAnyElementContainingText("New Customer");
        assertTrue(newOrder.isDisplayed());
    }
}