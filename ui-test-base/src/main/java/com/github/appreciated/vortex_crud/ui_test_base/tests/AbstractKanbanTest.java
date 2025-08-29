package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Base test for Kanban views.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractKanbanTest extends BaseUITest {

    protected String getPath() {
        return "tasks";
    }

    protected String getFilterValuePresent() {
        return "Task A";
    }

    protected String getFilterValueAbsent() {
        return "Task B";
    }

    /**
     * @return the column titles that should be present
     */
    protected String[] getExpectedColumnTitles() {
        return new String[]{"Option1", "Option2", "Option3"};
    }

    @Test
    void testKanbanColumnsVisible() {
        navigateTo(getPath());
        for (String column : getExpectedColumnTitles()) {
            waitForAnyElementContainingText(column);
        }
    }

    private WebElement getGridForColumn(String title) {
        WebElement header = waitForElement(By.xpath("//h4[contains(text(), '" + title + "')]"));
        WebElement wrapper = header.findElement(By.xpath("../.."));
        return wrapper.findElement(By.tagName("vaadin-grid"));
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
        List<WebElement> hidden = driver.findElements(By.xpath("//*[contains(text(), '" + absent + "')]") )
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
    void testDragAndDropUpdatesStatus() {
        navigateTo(getPath());
        WebElement sourceGrid = getGridForColumn(getExpectedColumnTitles()[0]);
        WebElement targetGrid = getGridForColumn(getExpectedColumnTitles()[1]);
        WebElement sourceRow = sourceGrid.getShadowRoot().findElement(By.cssSelector("tbody tr"));
        String taskText = sourceRow.getText();
        WebElement targetBody = targetGrid.getShadowRoot().findElement(By.cssSelector("tbody"));
        new Actions(driver).dragAndDrop(sourceRow, targetBody).perform();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        wait.until(d -> targetGrid.getShadowRoot()
                .findElements(By.cssSelector("tbody tr"))
                .stream()
                .anyMatch(tr -> tr.getText().contains(taskText)));
    }

    @Test
    void testDragAndDropReordersWithinColumn() {
        navigateTo(getPath());
        WebElement grid = getGridForColumn(getExpectedColumnTitles()[0]);
        WebElement firstRow = grid.getShadowRoot().findElement(By.cssSelector("tbody tr"));
        String text = firstRow.getText();
        WebElement body = grid.getShadowRoot().findElement(By.cssSelector("tbody"));
        new Actions(driver).dragAndDrop(firstRow, body).perform();
        wait.until(d -> {
            var rows = grid.getShadowRoot().findElements(By.cssSelector("tbody tr"));
            return !rows.isEmpty() && rows.get(rows.size() - 1).getText().contains(text);
        });
        driver.navigate().refresh();
        WebElement refreshedGrid = getGridForColumn(getExpectedColumnTitles()[0]);
        wait.until(d -> {
            var rows = refreshedGrid.getShadowRoot().findElements(By.cssSelector("tbody tr"));
            return !rows.isEmpty() && rows.get(rows.size() - 1).getText().contains(text);
        });
    }
}
