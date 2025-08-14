package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for card based project listings.
 */
public abstract class AbstractCardTest extends BaseUITest {

    protected String getPath() {
        return "images-grid";
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
        WebElement filter = waitForElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        filter.sendKeys(present);
        filter.sendKeys(Keys.ENTER);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        waitForAnyElementContainingText(present);
        List<WebElement> hidden = driver.findElements(By.xpath("//*[contains(text(), '" + absent + "')]"))
                .stream()
                .filter(WebElement::isDisplayed)
                .toList();
        assertTrue(hidden.isEmpty());
        filter.sendKeys(Keys.BACK_SPACE);
        filter.sendKeys(Keys.BACK_SPACE);
        filter.sendKeys(Keys.BACK_SPACE);
        waitForAnyElementContainingText(absent);
    }

    @Test
    void testCreateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Create").click();
        WebElement field = waitForElement(By.xpath("//vaadin-dialog-overlay//vaadin-text-field"))
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
        assertTrue(elements.stream().noneMatch(WebElement::isDisplayed));
    }

    @Test
    void testImagesDisplayed() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue());
        WebElement img = waitForElement(By.tagName("img"));
        assertTrue(img.isDisplayed());
    }
}
