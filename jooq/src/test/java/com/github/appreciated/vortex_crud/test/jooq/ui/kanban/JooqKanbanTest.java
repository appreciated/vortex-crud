package com.github.appreciated.vortex_crud.test.jooq.ui.kanban;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractKanbanTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "kanban_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqKanbanTest extends AbstractKanbanTest {

}
