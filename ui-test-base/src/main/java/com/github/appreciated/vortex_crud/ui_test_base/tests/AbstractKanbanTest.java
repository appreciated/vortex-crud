package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.options.BoundingBox;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
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
        String taskText = getTaskTitle(sourceGrid, sourceRow);
        Locator targetBody = targetGrid.locator("tbody#items");

        // Use Playwright's drag and drop with force option to bypass element interception
        sourceRow.dragTo(targetBody, new Locator.DragToOptions().setForce(true));

        page.waitForTimeout(3000);

        // Check if taskText is now in targetGrid
        List<Locator> targetRows = targetGrid.locator("tbody#items tr").all();
        assertTrue(targetRows.stream().anyMatch(row -> getTaskTitle(targetGrid, row).contains(taskText)));
    }

    @Test
    void testDragAndDropReordersWithinColumn() {
        navigateTo(getPath());

        Locator grid = getGridForColumn(getExpectedColumnTitles()[0]);
        Locator firstRow = grid.locator("tbody#items tr").first();
        String text = getTaskTitle(grid, firstRow);
        Locator lastRow = grid.locator("tbody#items tr").last();

        // Use Playwright's drag and drop with force option to bypass element interception
        firstRow.dragTo(lastRow, new Locator.DragToOptions().setForce(true));

        // Wait for reorder
        page.waitForTimeout(1000);

        List<Locator> rows = grid.locator("tbody#items tr").all();
        assertTrue(!rows.isEmpty() && getTaskTitle(grid, rows.get(rows.size() - 1)).contains(text));

        page.reload();

        Locator refreshedGrid = getGridForColumn(getExpectedColumnTitles()[0]);
        // Wait for grid to load
        refreshedGrid.waitFor();
        page.waitForTimeout(1000);

        List<Locator> refreshedRows = refreshedGrid.locator("tbody#items tr").all();
        assertTrue(!refreshedRows.isEmpty() && getTaskTitle(refreshedGrid, refreshedRows.get(refreshedRows.size() - 1)).contains(text));
    }

    @Test
    void testFractionalIndexReordering() {
        navigateTo(getPath());

        Locator col1 = getGridForColumn(getExpectedColumnTitles()[0]);
        Locator col2 = getGridForColumn(getExpectedColumnTitles()[1]);

        // Wait for rows to load in Col 1
        Locator items1 = col1.locator("tbody#items tr");
        items1.first().waitFor();

        // Ensure we have enough items to test
        assertTrue(items1.count() >= 2, "Column 1 needs at least 2 items for this test");

        Locator items2 = col2.locator("tbody#items tr");
        items2.first().waitFor();
        assertTrue(items2.count() >= 1, "Column 2 needs at least 1 item for this test");

        // Capture initial state dynamically
        String textA = getTaskTitle(col1, items1.nth(0));
        String textB = getTaskTitle(col1, items1.nth(1));

        Locator taskB = items1.nth(1);
        Locator taskC = items2.nth(0);
        String textC = getTaskTitle(col2, taskC);

        // Perform manual drag without explicit server wait (simplified for robustness)
        // Drag C to B, aiming for top edge to drop above (between A and B)
        dragAndDrop(taskC, taskB);

        page.waitForTimeout(1000);

        // Verify order in Col 1: A, C, B
        verifyColumnOrder(col1, items1, textA, textC, textB);

        // Verify persistence
        page.reload();
        col1 = getGridForColumn(getExpectedColumnTitles()[0]);
        col1.waitFor();
        Locator refreshedItems1 = col1.locator("tbody#items tr");
        refreshedItems1.first().waitFor();
        page.waitForTimeout(1000);

        verifyColumnOrder(col1, refreshedItems1, textA, textC, textB);
    }

    private void dragAndDrop(Locator source, Locator target) {
        source.hover();
        page.mouse().down();

        // Move mouse slightly to initiate drag event
        BoundingBox box = source.boundingBox();
        page.mouse().move(box.x + box.width / 2 + 10, box.y + box.height / 2 + 10);
        page.waitForTimeout(200); // Wait for drag start

        // Move to target
        BoundingBox targetBox = target.boundingBox();
        // Drop on top 25% of target to trigger "Above" (Between A and B)
        double dropX = targetBox.x + targetBox.width / 2;
        double dropY = targetBox.y + targetBox.height * 0.25;

        // Move in steps to ensure drag event is processed
        page.mouse().move(dropX, dropY, new Mouse.MoveOptions().setSteps(5));
        page.waitForTimeout(500); // Wait for drop target activation
        page.mouse().up();
    }

    private String getTaskTitle(Locator grid, Locator row) {
        String text = row.innerText();
        if (!text.trim().isEmpty()) return text;

        Locator slot = row.locator("slot").first();
        if (slot.count() > 0) {
            String slotName = slot.getAttribute("name");
            if (slotName != null) {
                // Find content in grid (Light DOM) with matching slot
                Locator content = grid.locator("vaadin-grid-cell-content[slot='" + slotName + "']");
                if (content.count() > 0) {
                    Locator h4 = content.locator("h4");
                    if (h4.count() > 0) return h4.innerText();
                    return content.innerText();
                }
            }
        }
        return "";
    }

    private void verifyColumnOrder(Locator grid, Locator items, String... expectedTitles) {
        // Assert we have at least as many items as expected
        assertTrue(items.count() >= expectedTitles.length, "Column has fewer items than expected. Found: " + items.count() + ", Expected at least: " + expectedTitles.length);

        List<String> foundTitles = new ArrayList<>();
        for (int i = 0; i < expectedTitles.length; i++) {
            String text = getTaskTitle(grid, items.nth(i));
            foundTitles.add(text);
            assertTrue(text.contains(expectedTitles[i]), "Expected " + expectedTitles[i] + " at index " + i + " but found " + text + ". Checked list: " + foundTitles);
        }
    }
}
