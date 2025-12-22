package com.github.appreciated.vortex_crud.test.jpa.ui.kanban;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractKanbanTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "kanban_test.sql")
public class JpaKanbanConfigurationConfigurationTest extends AbstractKanbanTest {
}
