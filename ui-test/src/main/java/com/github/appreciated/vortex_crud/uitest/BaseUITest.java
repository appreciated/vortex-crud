package com.github.appreciated.vortex_crud.uitest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.List;

/**
 * Base class for UI tests that provides common setup, teardown, and utility methods.
 */
@SpringBootTest
@ContextConfiguration(locations = "classpath:/test-context.xml")
public abstract class BaseUITest {
    
    protected static final String BASE_URL = "http://127.0.0.1:8080";
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    @BeforeAll
    public static void setupClass() {
        // Setup WebDriverManager to handle driver binaries
        WebDriverManager.chromedriver().setup();
    }
    
    @BeforeEach
    public void setupTest() {
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode for CI environments
        options.addArguments("--window-size=1920,1080"); // Set window size
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        // Initialize the WebDriver
        driver = new ChromeDriver(options);
        
        // Initialize the WebDriverWait with a timeout of 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    @AfterEach
    public void teardown() {
        // Close the browser after each test
        if (driver != null) {
            driver.quit();
        }
    }
    
    /**
     * Navigate to a specific URL path relative to the base URL.
     *
     * @param path the path to navigate to
     */
    protected void navigateTo(String path) {
        driver.get(BASE_URL+ "/" + path);
    }
    
    /**
     * Wait for an element to be visible and return it.
     *
     * @param by the locator to find the element
     * @return the visible WebElement
     */
    protected WebElement waitForElement(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    
    /**
     * Wait for elements to be visible and return them.
     *
     * @param by the locator to find the elements
     * @return the list of visible WebElements
     */
    protected List<WebElement> waitForElements(By by) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }
    
    /**
     * Wait for an element to be clickable and return it.
     *
     * @param by the locator to find the element
     * @return the clickable WebElement
     */
    protected WebElement waitForClickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }
    
    /**
     * Check if an element exists on the page.
     *
     * @param by the locator to find the element
     * @return true if the element exists, false otherwise
     */
    protected boolean elementExists(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for the page to load completely.
     */
    protected void waitForPageLoad() {
        wait.until(driver -> {
            String state = (String) ((ChromeDriver) driver).executeScript("return document.readyState");
            return "complete".equals(state);
        });
    }
}