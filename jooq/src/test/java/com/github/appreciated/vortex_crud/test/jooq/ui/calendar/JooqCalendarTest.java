package com.github.appreciated.vortex_crud.test.jooq.ui.calendar;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCalendarTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "calendar_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqCalendarTest extends AbstractCalendarTest {
}
