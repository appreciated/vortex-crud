package com.github.appreciated.vortex_crud.test.jpa.ui.calendar;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCalendarTest;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.jdbc.Sql;

@Disabled("FullCalendar library is incompatible with Vaadin 25 (uses legacy elemental.json API)")
@Sql(scripts = "calendar_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaCalendarTest extends AbstractCalendarTest {
}
