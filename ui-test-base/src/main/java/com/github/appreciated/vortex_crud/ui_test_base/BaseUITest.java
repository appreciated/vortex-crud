package com.github.appreciated.vortex_crud.ui_test_base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * Base class for UI tests that provides common setup, teardown, and utility methods using Playwright.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "vaadin.productionMode=true")
public abstract class BaseUITest {

    public static final int SECONDS = 30;
    @Value(value = "${local.server.port}")
    private int port;

    @Value(value = "${ui-test.disable-headless:false}")
    private boolean disableHeadless;

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    public Page getPage() {
        return page;
    }

    public BrowserContext getContext() {
        return context;
    }

    @BeforeAll
    public static void setupClass() {
        playwright = Playwright.create();
    }

    @BeforeEach
    public void setupTest() throws IOException {
        if (browser == null) {
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            options.setHeadless(!disableHeadless);
            browser = playwright.chromium().launch(options);
        }

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setLocale("en-US"));

        // Start tracing
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();
        page.setDefaultTimeout(SECONDS * 1000);
    }

    @AfterEach
    public void tearDownTest() throws IOException {
        if (context != null) {
            context.close();
        }
    }

    /**
     * Navigate to a specific URL path relative to the base URL.
     *
     * @param path the path to navigate to
     */
    protected void navigateTo(String path) {
        page.navigate(getUrl(path));
        waitForUrlToBe(path);
    }

    private String getUrl(String path) {
        return "http://127.0.0.1:%s/%s".formatted(port, path);
    }

    /**
     * Wait for an element to be visible and return it.
     *
     * @param selector the selector to find the element
     * @return the visible Locator
     */
    protected Locator waitForElement(String selector) {
        Locator locator = page.locator(selector).first();
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return locator;
    }

    protected Locator waitForAnyElementContainingText(String text) {
        // XPath approximation of original logic
        return waitForElement("//*[contains(text(), '%s')]".formatted(text));
    }

    protected Locator waitForElementContainingText(String xPath, String text) {
        String pattern = "//%s[contains(., '%s')]".formatted(xPath, text);
        return waitForElement(pattern);
    }

    protected void waitForUrlToBe(String path) {
        page.waitForURL(getUrl(path), new Page.WaitForURLOptions().setTimeout(SECONDS * 1000));
    }

    /**
     * Wait for elements to be visible and return them.
     *
     * @param selector the selector to find the elements
     * @return the list of visible Locators
     */
    protected List<Locator> waitForElements(String selector) {
        Locator locator = page.locator(selector).first();
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(selector).all();
    }

    protected Locator waitForElementWithTagAndValue(String tagName, String value) {
        try {
            page.waitForFunction("([t, v]) => Array.from(document.querySelectorAll(t)).some(el => el.value && el.value.toString().startsWith(v))",
                List.of(tagName, value),
                new Page.WaitForFunctionOptions().setTimeout(SECONDS * 1000));
        } catch (Exception e) {
             throw new RuntimeException("Timeout waiting for " + tagName + " with value " + value);
        }

        return page.locator(tagName).all().stream()
             .filter(loc -> {
                 Object val = loc.evaluate("el => el.value");
                 return val != null && val.toString().startsWith(value);
             })
             .findFirst()
             .orElseThrow(() -> new RuntimeException("Element not found: " + tagName + " with value " + value));
    }

    protected Locator waitForAnyElementContainingTextWithAttribute(String path, String text, String attributeName) {
        String xpathPattern = "//%s[@%s]/*[contains(text(), '%s')]".formatted(path, attributeName, text);
        return waitForElement(xpathPattern);
    }

    protected Locator waitForElementWithTagAndInputValue(String tagName, String value) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < SECONDS * 1000) {
            List<Locator> candidates = page.locator(tagName).locator("input").all();
            for (Locator cand : candidates) {
                if (cand.isVisible()) {
                     String val = cand.inputValue();
                     if (val != null && val.startsWith(value)) {
                         return cand;
                     }
                }
            }
            page.waitForTimeout(100);
        }
        throw new RuntimeException("Timeout waiting for input inside " + tagName + " with value " + value);
    }

    protected boolean isDisplayedSafe(Locator element) {
        return element.isVisible();
    }

}
