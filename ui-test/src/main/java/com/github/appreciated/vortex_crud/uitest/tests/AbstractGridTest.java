package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Base test for grid based project listings. Concrete implementations
 * must provide the route path and expected values.
 */
public abstract class AbstractGridTest extends BaseUITest {

    /**
     * @return route path to the projects grid
     */
    protected abstract String getPath();

    /**
     * @return value expected to be visible in the grid
     */
    protected abstract String getExpectedVisibleValue();

    /**
     * @return value that should remain after filtering, or {@code null} if no filter is configured
     */
    protected String getFilterValuePresent() {
        return null;
    }

    /**
     * @return value that should disappear after filtering, or {@code null} if no filter is configured
     */
    protected String getFilterValueAbsent() {
        return null;
    }

    /**
     * @return id of the entity expected when opening details; defaults to "1"
     */
    protected String getDetailId() {
        return "1";
    }

    @Test
    void testGridListingVisible() {
        navigateTo(getPath());
        WebElement element = waitForAnyElementContainingText(getExpectedVisibleValue());
        assertEquals("vaadin-grid-cell-content", element.getTagName());
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
            return; // no filter configured
        }
        navigateTo(getPath());
        WebElement filter = driver.findElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        filter.clear();
        filter.sendKeys(present);
        waitForAnyElementContainingText(present);
        List<WebElement> hidden = driver.findElements(By.xpath("//*[contains(text(), '" + absent + "')]"));
        assertEquals(0, hidden.size());
        filter.clear();
        waitForAnyElementContainingText(absent);
    }
}
