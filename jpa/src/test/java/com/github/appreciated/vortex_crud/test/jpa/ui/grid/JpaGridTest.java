package com.github.appreciated.vortex_crud.test.jpa.ui.grid;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractGridTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "projects_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaGridTest extends AbstractGridTest {
    @Override
    protected String getPath() {
        return "projects-list";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "Project Alpha";
    }

    @Override
    protected String getFilterValuePresent() {
        return "Project Alpha";
    }

    @Override
    protected String getFilterValueAbsent() {
        return "Project Beta";
    }
}
