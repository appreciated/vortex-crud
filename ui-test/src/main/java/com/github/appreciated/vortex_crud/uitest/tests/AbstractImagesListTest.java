package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for list views containing images.
 */
public abstract class AbstractImagesListTest extends BaseUITest {

    protected abstract String getPath();

    protected abstract String getExpectedVisibleValue();

    protected String getFilterValuePresent() { return null; }

    protected String getFilterValueAbsent() { return null; }

    @Test
    void testImageListVisible() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue());
        WebElement img = waitForElement(By.tagName("img"));
        assertTrue(img.isDisplayed());
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
        List<WebElement> hidden = driver.findElements(By.xpath("//*[contains(text(), '" + absent + "')]") );
        assertTrue(hidden.isEmpty());
        filter.clear();
        waitForAnyElementContainingText(absent);
    }
}
