package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for grid based project listings. Concrete implementations
 * must provide the route path and expected values.
 */
public abstract class AbstractGridTest extends BaseUITest {

    protected String getPath() {
        return "images-list";
    }

    protected String getExpectedVisibleValue() {
        return "Red";
    }

    protected String getFilterValuePresent() {
        return "Red";
    }

    protected String getFilterValueAbsent() {
        return "Green";
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
                .filter(WebElement::isDisplayed)
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
    void testImagesDisplayed() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue());
        WebElement img = waitForElement(By.tagName("img"));
        assertTrue(img.isDisplayed());
    }
}
