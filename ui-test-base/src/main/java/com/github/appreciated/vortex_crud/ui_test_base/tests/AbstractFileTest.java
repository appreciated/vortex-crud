package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for generic file field functionality.
 */
public abstract class AbstractFileTest extends BaseUITest {

    protected String getPath() {
        return "generic-files-grid";
    }

    protected String getExpectedVisibleValue() {
        return "Sample Document";
    }

    protected String getFilterValuePresent() {
        return "Sample";
    }

    protected String getFilterValueAbsent() {
        return "Nonexistent";
    }

    protected String getDetailId() {
        return "1";
    }

    @Test
    void testFileListingVisible() {
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
                .filter(this::isDisplayedSafe)
                .toList();
        assertTrue(hidden.isEmpty());
        filter.sendKeys(Keys.BACK_SPACE);
        filter.sendKeys(Keys.BACK_SPACE);
        filter.sendKeys(Keys.BACK_SPACE);
        filter.sendKeys(Keys.BACK_SPACE);
        filter.sendKeys(Keys.BACK_SPACE);
        filter.sendKeys(Keys.BACK_SPACE);
        waitForAnyElementContainingText(absent);
    }

    @Test
    void testCreateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Create").click();
        WebElement field = waitForElement(By.xpath("//vaadin-dialog//vaadin-text-field"))
                .findElement(By.tagName("input"));
        field.sendKeys("Created File Entry");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Created File Entry");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue()).click();
        waitForUrlToBe(getPath() + "/" + getDetailId());
        WebElement field = waitForElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        field.clear();
        field.sendKeys("Updated File Entry");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Updated File Entry");
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

    @Test
    void testFileNameDisplayed() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue());
        // File field should show filename in the UI
        WebElement fileName = waitForAnyElementContainingText("sample.docx");
        assertTrue(fileName.isDisplayed());
    }
}
