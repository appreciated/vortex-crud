package com.github.appreciated.vortex_crud.test.jpa.ui.calendar;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCalendarTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "calendar_test.sql")
public class JpaCalendarTest extends AbstractCalendarTest {
}
