package com.github.appreciated.vortex_crud.ui_test_base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * Base class for UI tests that provides common setup, teardown, and utility methods using Playwright.
 */
@ExtendWith(PlaywrightTraceExtension.class)
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

    @AfterAll
    public static void tearDownClass() {
        if (browser != null) {
            try {
                browser.close();
            } catch (Exception e) {
                System.err.println("Error closing browser: " + e.getMessage());
            }
            browser = null;
        }

        if (playwright != null) {
            try {
                playwright.close();
            } catch (Exception e) {
                System.err.println("Error closing playwright: " + e.getMessage());
            }
            playwright = null;
        }
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
            // Handle shadow DOM selectors like "vaadin-date-time-picker//vaadin-date-picker"
            if (tagName.contains("//")) {
                String[] parts = tagName.split("//", 2);
                String parentSelector = parts[0];
                String childSelector = parts[1];

                page.waitForFunction("([parent, child, v]) => {" +
                    "  const parents = document.querySelectorAll(parent);" +
                    "  for (let p of parents) {" +
                    "    if (p.shadowRoot) {" +
                    "      const children = p.shadowRoot.querySelectorAll(child);" +
                    "      for (let c of children) {" +
                    "        if (c.value && c.value.toString().startsWith(v)) return true;" +
                    "      }" +
                    "    }" +
                    "  }" +
                    "  return false;" +
                    "}",
                    List.of(parentSelector, childSelector, value),
                    new Page.WaitForFunctionOptions().setTimeout(SECONDS * 1000));

                // Return the parent locator as Playwright will pierce shadow DOM
                return page.locator(parentSelector).first();
            } else {
                page.waitForFunction("([t, v]) => Array.from(document.querySelectorAll(t)).some(el => el.value && el.value.toString().startsWith(v))",
                    List.of(tagName, value),
                    new Page.WaitForFunctionOptions().setTimeout(SECONDS * 1000));

                return page.locator(tagName).all().stream()
                     .filter(loc -> {
                         Object val = loc.evaluate("el => el.value");
                         return val != null && val.toString().startsWith(value);
                     })
                     .findFirst()
                     .orElseThrow(() -> new RuntimeException("Element not found: " + tagName + " with value " + value));
            }
        } catch (Exception e) {
             throw new RuntimeException("Timeout waiting for " + tagName + " with value " + value, e);
        }
    }

    /**
     * Wait for an element containing the given text to disappear from the DOM.
     *
     * @param text the text to search for
     */
    protected void waitForTextToDisappear(String text) {
        page.waitForFunction(
            "text => !Array.from(document.querySelectorAll('*')).some(el => el.textContent && el.textContent.includes(text))",
            text,
            new Page.WaitForFunctionOptions().setTimeout(SECONDS * 1000)
        );
    }

    /**
     * Wait for elements matching the given XPath to be detached or hidden.
     *
     * @param xpath the XPath selector
     */
    protected void waitForElementsToBeHidden(String xpath) {
        page.waitForFunction(
            "xpath => {" +
            "  const result = document.evaluate(xpath, document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);" +
            "  if (result.snapshotLength === 0) return true;" +
            "  for (let i = 0; i < result.snapshotLength; i++) {" +
            "    const el = result.snapshotItem(i);" +
            "    if (el.offsetParent !== null || window.getComputedStyle(el).display !== 'none') return false;" +
            "  }" +
            "  return true;" +
            "}",
            xpath,
            new Page.WaitForFunctionOptions().setTimeout(SECONDS * 1000)
        );
    }
}
