package com.github.appreciated.vortex_crud.test.jooq.ui.projects;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractProjectsCardTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "projects_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqProjectsCardTest extends AbstractProjectsCardTest {
    @Override
    protected String getPath() {
        return "projects-cards";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "Project Alpha";
    }
}
