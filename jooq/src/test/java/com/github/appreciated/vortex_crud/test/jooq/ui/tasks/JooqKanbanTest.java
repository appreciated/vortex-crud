package com.github.appreciated.vortex_crud.test.jooq.ui.tasks;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractKanbanTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jooq/ui/tasks/tasks_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqKanbanTest extends AbstractKanbanTest {
    @Override
    protected String getPath() {
        return "tasks/open";
    }
}
