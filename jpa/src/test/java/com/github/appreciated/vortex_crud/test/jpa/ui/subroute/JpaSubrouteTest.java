package com.github.appreciated.vortex_crud.test.jpa.ui.subroute;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractSubrouteTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jpa/ui/kanban/kanban_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaSubrouteTest extends AbstractSubrouteTest {
    @Override
    protected String getParentPath() {
        return "tasks";
    }

    @Override
    protected String getChildLabel() {
        return "Open";
    }

    @Override
    protected String getChildPath() {
        return "tasks/open";
    }
}
