package com.github.appreciated.vortex_crud.test.jpa.ui.card;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractCardTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jpa/ui/projects/projects_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaCardTest extends AbstractCardTest {
    @Override
    protected String getPath() {
        return "projects-cards";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "Project Alpha";
    }
}
