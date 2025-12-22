package com.github.appreciated.vortex_crud.test.jooq.ui.grid;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractGridTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("grid_test.sql")
public class JooqGridTest extends AbstractGridTest {
    @Override
    protected boolean hasDefaultFilter() {
        return true;
    }

    @Override
    protected String getDefaultFilterPath() {
        return "filtered-grid";
    }

    @Override
    protected String getDefaultFilterVisibleValue() {
        return "ItemOne";
    }

    @Override
    protected String getDefaultFilterHiddenValue() {
        return "ItemTwo";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "ItemOne";
    }

    @Override
    protected String getFilterValuePresent() {
        return "ItemOne";
    }

    @Override
    protected String getFilterValueAbsent() {
        return "ItemTwo";
    }
}

