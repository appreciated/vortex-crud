package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for card based project listings.
 */
public abstract class AbstractProjectsCardTest extends BaseUITest {

    protected abstract String getPath();

    protected abstract String getExpectedVisibleValue();

    protected String getFilterValuePresent() {
        return null;
    }

    protected String getFilterValueAbsent() {
        return null;
    }

    protected String getDetailId() {
        return "1";
    }

    @Test
    void testCardListingVisible() {
        navigateTo(getPath());
        WebElement element = waitForAnyElementContainingText(getExpectedVisibleValue());
        assertTrue(element.isDisplayed());
    }

    @Test
    void testNavigateToDetail() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue()).click();
        waitForUrlToBe(getPath() + "/" + getDetailId());
    }

    @Test
    void testFilterIfAvailable() {
        String present = getFilterValuePresent();
        String absent = getFilterValueAbsent();
        if (present == null || absent == null) {
            return;
        }
        navigateTo(getPath());
        WebElement filter = driver.findElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        filter.clear();
        filter.sendKeys(present);
        waitForAnyElementContainingText(present);
        List<WebElement> hidden = driver.findElements(By.xpath("//*[contains(text(), '" + absent + "')]"));
        assertTrue(hidden.isEmpty());
        filter.clear();
        waitForAnyElementContainingText(absent);
    }
}
