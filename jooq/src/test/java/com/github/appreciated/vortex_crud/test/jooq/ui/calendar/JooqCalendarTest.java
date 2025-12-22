package com.github.appreciated.vortex_crud.test.jooq.ui.calendar;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCalendarTest;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.jdbc.Sql;

@Disabled("FullCalendar library is incompatible with Vaadin 25 (uses legacy elemental.json API)")
@Sql("calendar_test.sql")
public class JooqCalendarTest extends AbstractCalendarTest {
}
