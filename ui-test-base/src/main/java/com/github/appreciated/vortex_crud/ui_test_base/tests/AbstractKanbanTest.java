package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Base test for Kanban views.
 */
public abstract class AbstractKanbanTest extends BaseUITest {

    protected String getPath() {
        return "tasks";
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
        WebElement wrapper = header.findElement(By.xpath("./ancestor::div[contains(@class,'no-hover')]"));
        return wrapper.findElement(By.tagName("vaadin-grid"));
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
        wait.until(d -> targetGrid.getShadowRoot()
                .findElements(By.cssSelector("tbody tr"))
                .stream()
                .anyMatch(tr -> tr.getText().contains(taskText)));
    }
}
