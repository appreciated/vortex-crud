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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.List;

/**
 * Base class for UI tests that provides common setup, teardown, and utility methods.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(locations = "classpath:/test-context.xml")
@EnableAutoConfiguration
public abstract class BaseUITest {

    @Value(value = "${local.server.port}")
    private int port;

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        // Initialize the WebDriver
        driver = new ChromeDriver(new ChromeOptions());

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
        driver.get(getUrl(path));
    }

    private String getUrl(String path) {
        return "http://127.0.0.1:%s/%s".formatted(port, path);
    }

    /**
     * Wait for an element to be visible and return it.
     *
     * @param by the locator to find the element
     * @return the visible WebElement
     */
    protected WebElement waitForElement(By by) {
        return wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement waitForElementContainingText(String text) {
        return waitForElement(By.xpath("//*[contains(text(), '%s')]".formatted(text)));
    }

    protected void waitForUrlToBe(String path) {
        wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.urlToBe(getUrl(path)));
    }

    /**
     * Wait for elements to be visible and return them.
     *
     * @param by the locator to find the elements
     * @return the list of visible WebElements
     */
    protected List<WebElement> waitForElements(By by) {
        return wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    protected boolean hasElementContainingText(String text) {
        waitForElementContainingText(text);
        return true;
    }

    protected boolean hasElementWithTagAndValue(String tagName, String value) {
        waitForElementWithTagAndValue(tagName, value);
        return true;
    }

    protected boolean hasElementWithTagAndInputValue(String tagName, String value) {
        waitForElementWithTagAndInputValue(tagName, value);
        return true;
    }

    protected WebElement waitForElementWithTagAndValue(String tagName, String value) {
        return waitForElements(By.tagName(tagName)).stream()
                .filter(webElement -> webElement.getAttribute("value").startsWith(value))
                .findFirst()
                .orElseThrow();
    }

    protected WebElement waitForElementWithTagAndInputValue(String tagName, String value) {
        return waitForElements(By.tagName(tagName)).stream()
                .filter(webElement -> {
                    WebElement input = webElement.findElement(By.tagName("input"));
                    return input.getAttribute("value").startsWith(value);
                })
                .findFirst()
                .orElseThrow();
    }

}