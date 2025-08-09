package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for one-to-many relationship tests.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public abstract class AbstractOneToManyTest extends BaseUITest {

    /**
     * Get the path to use for departments tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for departments tests
     */
    private String getDepartmentsPath() {
        return "departments-test";
    }

    /**
     * Get the path to use for employees tests.
     * This should be implemented by the concrete test class.
     *
     * @return the path for employees tests
     */
    private String getEmployeesPath() {
        return "employees-test";
    }

    @Test
    void testDepartmentsListingVisible() {
        navigateTo(getDepartmentsPath());
        WebElement webElement = waitForAnyElementContainingText("Engineering");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void testDepartmentNavigationPossible() {
        navigateTo(getDepartmentsPath());
        waitForAnyElementContainingText("Engineering").click();
        waitForUrlToBe(getDepartmentsPath() + "/1");
        
        // Verify department details
        waitForElementWithTagAndValue("vaadin-text-field", "Engineering");
        waitForElementWithTagAndValue("vaadin-text-field", "Building A");
        
        // Verify employees are displayed
        waitForAnyElementContainingText("John Smith");
        waitForAnyElementContainingText("Jane Doe");
        waitForAnyElementContainingText("Bob Johnson");
    }

    @Test
    void testEmployeesListingVisible() {
        navigateTo(getEmployeesPath());
        WebElement webElement = waitForAnyElementContainingText("John Smith");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void testEmployeeNavigationPossible() {
        navigateTo(getEmployeesPath());
        waitForAnyElementContainingText("John Smith").click();
        waitForUrlToBe(getEmployeesPath() + "/1");
        
        // Verify employee details
        waitForElementWithTagAndValue("vaadin-text-field", "John Smith");
        waitForElementWithTagAndValue("vaadin-email-field", "john@example.com");
        
        // Verify department selection
        waitForElementWithTagAndInputValue("vaadin-combo-box", "Engineering");
    }

    @Test
    void testEmployeeCreation() {
        navigateTo(getEmployeesPath() + "/new");
        
        // Fill employee details
        WebElement nameField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        nameField.sendKeys("New Employee");
        
        WebElement emailField = driver.findElement(By.cssSelector("vaadin-email-field"));
        emailField.sendKeys("new@example.com");
        
        // Select department
        WebElement departmentField = driver.findElement(By.cssSelector("vaadin-combo-box"));
        departmentField.click();
        
        // Wait for dropdown to open and select Engineering
        waitForAnyElementContainingText("Engineering").click();
        
        // Save the employee
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the employees list
        waitForUrlToBe(getEmployeesPath());
        
        // Verify the new employee appears
        WebElement newEmployee = waitForAnyElementContainingText("New Employee");
        assertTrue(newEmployee.isDisplayed());
    }

    @Test
    void testDepartmentCreationWithEmployees() {
        navigateTo(getDepartmentsPath() + "/new");
        
        // Fill department details
        WebElement nameField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        nameField.sendKeys("New Department");
        
        WebElement locationField = driver.findElement(By.cssSelector("vaadin-text-field"));
        locationField.sendKeys("Building D");
        
        // Save the department
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the departments list
        waitForUrlToBe(getDepartmentsPath());
        
        // Navigate to the new department
        waitForAnyElementContainingText("New Department").click();
        
        // Add an employee to the department
        waitForAnyElementContainingText("Add Employee").click();
        
        // Fill employee details
        WebElement employeeNameField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        employeeNameField.sendKeys("Department Employee");
        
        WebElement employeeEmailField = driver.findElement(By.cssSelector("vaadin-email-field"));
        employeeEmailField.sendKeys("dept@example.com");
        
        // Save the employee
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the department view
        // Note: We can't hardcode the ID here as it might vary
        // Instead, we'll check that we're back to a department URL
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains(getDepartmentsPath() + "/"));
        
        // Verify the new employee appears
        WebElement newEmployee = waitForAnyElementContainingText("Department Employee");
        assertTrue(newEmployee.isDisplayed());
    }

    @Test
    void testChangingEmployeeDepartment() {
        navigateTo(getEmployeesPath());
        waitForAnyElementContainingText("John Smith").click();
        waitForUrlToBe(getEmployeesPath() + "/1");
        
        // Change department
        WebElement departmentField = driver.findElement(By.cssSelector("vaadin-combo-box"));
        departmentField.click();
        
        // Wait for dropdown to open and select Marketing
        waitForAnyElementContainingText("Marketing").click();
        
        // Save the employee
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the employees list
        waitForUrlToBe(getEmployeesPath());
        
        // Navigate to Marketing department
        navigateTo(getDepartmentsPath());
        waitForAnyElementContainingText("Marketing").click();
        
        // Verify John Smith is now in Marketing
        WebElement employee = waitForAnyElementContainingText("John Smith");
        assertTrue(employee.isDisplayed());
    }
}