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

    private Locator getGridForColumn(String title) {
        Locator header = waitForElement("//h4[contains(text(), '" + title + "')]");
        Locator wrapper = header.locator("xpath=../..");
        return wrapper.locator("vaadin-grid");
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
    void testDragAndDropUpdatesStatus() {
        navigateTo(getPath());

        Locator sourceGrid = getGridForColumn(getExpectedColumnTitles()[0]);
        Locator targetGrid = getGridForColumn(getExpectedColumnTitles()[1]);

        Locator sourceRow = sourceGrid.locator("tbody#items tr").first();
        String taskText = sourceRow.innerText();
        Locator targetBody = targetGrid.locator("tbody#items");

        // Use Playwright's drag and drop with force option to bypass element interception
        sourceRow.dragTo(targetBody, new Locator.DragToOptions().setForce(true));

        page.waitForTimeout(3000);

        // Check if taskText is now in targetGrid
        List<Locator> targetRows = targetGrid.locator("tbody#items tr").all();
        assertTrue(targetRows.stream().anyMatch(row -> row.innerText().contains(taskText)));
    }

    @Test
    void testDragAndDropReordersWithinColumn() {
        navigateTo(getPath());

        Locator grid = getGridForColumn(getExpectedColumnTitles()[0]);
        Locator firstRow = grid.locator("tbody#items tr").first();
        String text = firstRow.innerText();
        Locator lastRow = grid.locator("tbody#items tr").last();

        // Use Playwright's drag and drop with force option to bypass element interception
        firstRow.dragTo(lastRow, new Locator.DragToOptions().setForce(true));

        // Wait for reorder
        page.waitForTimeout(1000);

        List<Locator> rows = grid.locator("tbody#items tr").all();
        assertTrue(!rows.isEmpty() && rows.get(rows.size() - 1).innerText().contains(text));

        page.reload();

        Locator refreshedGrid = getGridForColumn(getExpectedColumnTitles()[0]);
        // Wait for grid to load
        refreshedGrid.waitFor();
        page.waitForTimeout(1000);

        List<Locator> refreshedRows = refreshedGrid.locator("tbody#items tr").all();
        assertTrue(!refreshedRows.isEmpty() && refreshedRows.get(refreshedRows.size() - 1).innerText().contains(text));
    }
}
