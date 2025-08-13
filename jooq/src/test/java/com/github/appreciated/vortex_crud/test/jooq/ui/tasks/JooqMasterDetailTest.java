package com.github.appreciated.vortex_crud.test.jooq.ui.tasks;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractMasterDetailTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jooq/ui/tasks/tasks_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqMasterDetailTest extends AbstractMasterDetailTest {
    @Override
    protected String getPath() {
        return "tasks/done";
    }
}
