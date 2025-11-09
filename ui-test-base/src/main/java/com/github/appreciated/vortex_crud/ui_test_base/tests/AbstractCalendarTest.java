package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for Calendar views.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractCalendarTest extends BaseUITest {

    protected String getPath() {
        return "calendar";
    }

    protected String getFilterValuePresent() {
        return "Event A";
    }

    protected String getFilterValueAbsent() {
        return "Event B";
    }

    /**
     * @return the expected calendar view title or identifier
     */
    protected String getExpectedCalendarTitle() {
        return "Calendar";
    }

    @Test
    void testCalendarVisible() {
        navigateTo(getPath());
        waitForElement(By.cssSelector("fc-full-calendar"));
    }

    @Test
    void testCalendarEventsVisible() {
        navigateTo(getPath());
        WebElement calendar = waitForElement(By.cssSelector("fc-full-calendar"));
        // Wait for calendar to render
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Check if there are events rendered in the calendar
        SearchContext shadowRoot = calendar.getShadowRoot();
        if (shadowRoot != null) {
            List<WebElement> events = shadowRoot.findElements(By.cssSelector(".fc-event"));
            assertTrue(events.size() > 0, "Calendar should have at least one event");
        }
    }

    @Test
    void testFilterIfAvailable() {
        String present = getFilterValuePresent();
        String absent = getFilterValueAbsent();
        if (present == null || absent == null) {
            return;
        }
        navigateTo(getPath());
        WebElement filter = waitForElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        filter.sendKeys(present);
        waitForElementWithTagAndValue("vaadin-text-field", present);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<WebElement> hidden = driver.findElements(By.xpath("//*[contains(text(), '" + absent + "')]"))
                .stream()
                .filter(this::isDisplayedSafe)
                .toList();
        assertEquals(0, hidden.size());
        for (int i = 0; i < present.length(); i++) {
            filter.sendKeys(Keys.BACK_SPACE);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        waitForAnyElementContainingText(absent);
    }

    @Test
    void testAddButtonVisible() {
        navigateTo(getPath());
        // Check if the add button is visible
        waitForElement(By.cssSelector("vaadin-button[theme~='primary']"));
    }

    @Test
    void testEventClickOpensDialog() {
        navigateTo(getPath());
        WebElement calendar = waitForElement(By.cssSelector("fc-full-calendar"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        SearchContext shadowRoot = calendar.getShadowRoot();
        if (shadowRoot != null) {
            List<WebElement> events = shadowRoot.findElements(By.cssSelector(".fc-event"));
            if (!events.isEmpty()) {
                events.get(0).click();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // Check if a dialog opened
                waitForElement(By.tagName("vaadin-dialog-overlay"));
            }
        }
    }
}
