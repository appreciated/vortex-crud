package com.github.appreciated.vortex_crud.test.jooq.ui.grid;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractGridTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "grid_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqGridTest extends AbstractGridTest {
}
