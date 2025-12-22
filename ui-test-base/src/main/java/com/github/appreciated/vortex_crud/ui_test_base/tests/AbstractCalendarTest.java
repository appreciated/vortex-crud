package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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
        waitForElement(".fc-view");
    }

    @Test
    void testCalendarEventsVisible() {
        navigateTo(getPath());
        waitForElement(".fc-view");
        // Wait for calendar to render
        page.waitForTimeout(1000);
        // Check if there are events rendered in the calendar
        // Playwright locators pierce shadow DOM by default
        List<Locator> events = page.locator(".fc-view .fc-event").all();
        assertTrue(events.size() > 0, "Calendar should have at least one event");
    }

    @Test
    void testFilterIfAvailable() {
        String present = getFilterValuePresent();
        String absent = getFilterValueAbsent();
        if (present == null || absent == null) {
            return;
        }
        navigateTo(getPath());
        Locator filter = waitForElement("vaadin-text-field").locator("input");
        filter.fill(present);
        waitForElementWithTagAndValue("vaadin-text-field", present);
        page.waitForTimeout(500);

        List<Locator> hidden = page.locator("//*[contains(text(), '" + absent + "')]").all()
                .stream()
                .filter(Locator::isVisible)
                .toList();
        assertEquals(0, hidden.size());

        for (int i = 0; i < present.length(); i++) {
            filter.press("Backspace");
        }
        page.waitForTimeout(500);
        waitForAnyElementContainingText(absent);
    }

    @Test
    void testAddButtonVisible() {
        navigateTo(getPath());
        // Check if the add button is visible
        waitForElement("vaadin-button[theme~='primary']");
    }

    @Test
    void testEventClickOpensDialog() {
        navigateTo(getPath());
        waitForElement(".fc-view");
        page.waitForTimeout(1000);

        List<Locator> events = page.locator(".fc-view .fc-event").all();
        if (!events.isEmpty()) {
            events.get(0).click();
            page.waitForTimeout(500);
            // Check if a dialog opened
            waitForElement("vaadin-dialog-overlay");
        }
    }
}
