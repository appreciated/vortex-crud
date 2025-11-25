package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
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
    void testImagesDisplayed() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue());
        WebElement img = waitForElement(By.tagName("img"));
        assertTrue(img.isDisplayed());
    }

    @Test
    void testCreateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Create").click();
        WebElement field = waitForElement(By.xpath("//vaadin-dialog//vaadin-text-field"))
                .findElement(By.tagName("input"));
        field.sendKeys("Created Entry");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Created Entry");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue()).click();
        waitForUrlToBe(getPath() + "/" + getDetailId());
        WebElement field = waitForElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        field.clear();
        field.sendKeys("Updated Entry");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Updated Entry");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue()).click();
        waitForUrlToBe(getPath() + "/" + getDetailId());
        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getPath());
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + getExpectedVisibleValue() + "')]"));
        assertTrue(elements.stream().noneMatch(this::isDisplayedSafe));
    }
}
