package com.github.appreciated.vortex_crud.test.jooq.ui.select_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSelectFieldRouteTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "select_field_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqSelectFieldTest extends AbstractSelectFieldRouteTest {
}
