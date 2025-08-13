package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;

/**
 * Base test for Kanban views.
 */
public abstract class AbstractKanbanTest extends BaseUITest {

    /**
     * @return the path to the Kanban view
     */
    protected abstract String getPath();

    /**
     * @return the column titles that should be present
     */
    protected String[] getExpectedColumnTitles() {
        return new String[]{"ToDo", "In progress"};
    }

    @Test
    void testKanbanColumnsVisible() {
        navigateTo(getPath());
        for (String column : getExpectedColumnTitles()) {
            waitForAnyElementContainingText(column);
        }
    }
}
