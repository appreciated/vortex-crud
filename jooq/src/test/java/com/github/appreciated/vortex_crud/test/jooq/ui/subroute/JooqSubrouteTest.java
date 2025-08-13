package com.github.appreciated.vortex_crud.test.jooq.ui.subroute;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractSubrouteTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jooq/ui/subroute/subroute_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqSubrouteTest extends AbstractSubrouteTest {
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
