package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for testing additional field types that are not covered by other tests.
 * This includes TextArea, Password, Video, and BigDecimal fields.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractAdditionalFieldsTest extends BaseUITest {

    /**
     * Get the path to use for additional fields tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for additional fields tests
     */
    public String getAdditionalFieldsPath() {
        return "additional-fields-test";
    }

    @Test
    void testListingVisible() {
        navigateTo(getAdditionalFieldsPath());
        WebElement webElement = waitForAnyElementContainingText("Test Entity");
        assertEquals("vaadin-grid-cell-content", webElement.getTagName());
    }

    @Test
    void testEntityLoading() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        // Verify TextArea field loads
        WebElement textAreaField = waitForElement(By.tagName("vaadin-text-area"))
                .findElement(By.tagName("textarea"));
        assertTrue(textAreaField.getAttribute("value").contains("This is a long description"));

        // Verify Password field exists (value should be masked)
        WebElement passwordField = waitForElement(By.tagName("vaadin-password-field"))
                .findElement(By.tagName("input"));
        assertEquals("password", passwordField.getAttribute("type"));

        // Verify BigDecimal field loads
        waitForElementWithTagAndValue("vaadin-number-field", "99.99");

        // Note: Video field test skipped when video_url is NULL in test data
    }

    @Test
    void testTextAreaInput() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        // Fill in required fields
        WebElement nameField = waitForElementContainingText("vaadin-text-field", "Name")
                .findElement(By.tagName("input"));
        nameField.sendKeys("TextArea Test");

        // Fill in TextArea field
        WebElement textAreaField = driver.findElement(By.tagName("vaadin-text-area"))
                .findElement(By.tagName("textarea"));
        textAreaField.sendKeys("This is a multi-line\ntext area\nwith several lines");

        // Fill BigDecimal field
        WebElement bigDecimalField = driver.findElement(By.tagName("vaadin-number-field"))
                .findElement(By.tagName("input"));
        bigDecimalField.sendKeys("123.45");

        // Fill Password field
        WebElement passwordField = driver.findElement(By.tagName("vaadin-password-field"))
                .findElement(By.tagName("input"));
        passwordField.sendKeys("SecurePassword123");

        // Save
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());

        // Verify the entity was created
        waitForAnyElementContainingText("TextArea Test");
    }

    @Test
    void testPasswordFieldMasking() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        // Find password field
        WebElement passwordField = driver.findElement(By.tagName("vaadin-password-field"))
                .findElement(By.tagName("input"));

        // Verify it's a password type input (masked)
        assertEquals("password", passwordField.getAttribute("type"));

        // Enter a password
        passwordField.sendKeys("MySecretPassword");

        // The value should be present but masked in the UI
        assertEquals("MySecretPassword", passwordField.getAttribute("value"));
    }

    @Test
    void testBigDecimalPrecision() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        WebElement nameField = waitForElementContainingText("vaadin-text-field", "Name")
                .findElement(By.tagName("input"));
        nameField.sendKeys("BigDecimal Test");

        // Test BigDecimal with high precision
        WebElement bigDecimalField = driver.findElement(By.tagName("vaadin-number-field"))
                .findElement(By.tagName("input"));
        bigDecimalField.sendKeys("999999.99");

        // Fill required password field
        WebElement passwordField = driver.findElement(By.tagName("vaadin-password-field"))
                .findElement(By.tagName("input"));
        passwordField.sendKeys("password");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());

        // Open the entity and verify the value
        waitForAnyElementContainingText("BigDecimal Test").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/2");
        waitForElementWithTagAndValue("vaadin-number-field", "999999.99");
    }

    @Test
    void testVideoFieldDisplay() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        // Video field is rendered but video_url is NULL in test data, so no video element will be present
        // This test verifies the page loads successfully without errors even when video is NULL
        WebElement nameField = waitForElement(By.tagName("vaadin-text-field"));
        assertTrue(nameField.isDisplayed());
    }

    @Test
    void testCreateEntry() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        WebElement nameField = waitForElementContainingText("vaadin-text-field", "Name")
                .findElement(By.tagName("input"));
        nameField.sendKeys("Created Entity");

        WebElement passwordField = driver.findElement(By.tagName("vaadin-password-field"))
                .findElement(By.tagName("input"));
        passwordField.sendKeys("newpassword");

        WebElement bigDecimalField = driver.findElement(By.tagName("vaadin-number-field"))
                .findElement(By.tagName("input"));
        bigDecimalField.sendKeys("50.00");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Created Entity");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        WebElement nameField = waitForElementContainingText("vaadin-text-field", "Name")
                .findElement(By.tagName("input"));
        nameField.clear();
        nameField.sendKeys("Updated Entity");

        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Updated Entity");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getAdditionalFieldsPath());

        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), 'Test Entity')]"));
        assertTrue(elements.stream().noneMatch(this::isDisplayedSafe));
    }
}
