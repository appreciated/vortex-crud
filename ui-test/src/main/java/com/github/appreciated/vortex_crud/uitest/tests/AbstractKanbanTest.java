package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;

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
}
