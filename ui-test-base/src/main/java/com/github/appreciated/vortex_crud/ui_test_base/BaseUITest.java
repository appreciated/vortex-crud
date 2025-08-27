package com.github.appreciated.vortex_crud.ui_test_base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;

/**
 * Base class for UI tests that provides common setup, teardown, and utility methods.
 */

@ExtendWith(ScreenshotExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "vaadin.productionMode=true")
public abstract class BaseUITest {

    public static final int SECONDS = 2;
    @Value(value = "${local.server.port}")
    private int port;

    protected WebDriver driver;
    protected WebDriverWait wait;
    private Path userDataDir;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() throws IOException {
        userDataDir = Files.createTempDirectory("ui-test-chrome-user-data-");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--lang=en");
        options.addArguments("--accept-lang=en");
        options.addArguments("--user-data-dir=" + userDataDir.toAbsolutePath());

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS));
    }

    @AfterEach
    public void tearDownTest() throws IOException {
        if (driver != null) {
            driver.quit();
        }
        if (userDataDir != null) {
            Files.walk(userDataDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    /**
     * Navigate to a specific URL path relative to the base URL.
     *
     * @param path the path to navigate to
     */
    protected void navigateTo(String path) {
        driver.get(getUrl(path));
        waitForUrlToBe(path);
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
        return wait.withTimeout(Duration.ofSeconds(SECONDS)).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement waitForAnyElementContainingText(String text) {
        return waitForElement(By.xpath("//*[contains(text(), '%s')]".formatted(text)));
    }

    protected WebElement waitForElementContainingText(String xPath, String text) {
        String pattern = "//%s[contains(., '%s')]".formatted(xPath, text);
        return waitForElement(By.xpath(pattern));
    }

    protected void waitForUrlToBe(String path) {
        wait.withTimeout(Duration.ofSeconds(SECONDS)).until(ExpectedConditions.urlToBe(getUrl(path)));
    }

    /**
     * Wait for elements to be visible and return them.
     *
     * @param by the locator to find the elements
     * @return the list of visible WebElements
     */
    protected List<WebElement> waitForElements(By by) {
        return wait.withTimeout(Duration.ofSeconds(SECONDS)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    protected WebElement waitForElementWithTagAndValue(String tagName, String value) {
        return waitForElements(By.xpath("//"+tagName)).stream()
                .filter(webElement -> webElement.getAttribute("value").startsWith(value))
                .findFirst()
                .orElseThrow();
    }

    protected WebElement waitForAnyElementContainingTextWithAttribute(String path, String text, String attributeName) {
        String xpathPattern = "//%s[@%s]/*[contains(text(), '%s')]".formatted(path, attributeName, text);
        return waitForElement(By.xpath(xpathPattern));
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

    protected boolean isDisplayedSafe(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

}