package com.github.appreciated.vortex_crud.test.jooq.ui.projects;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractCardTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "projects_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqCardTest extends AbstractCardTest {
    @Override
    protected String getPath() {
        return "projects-cards";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "Project Alpha";
    }
}
