package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateTimeFieldTest;
import org.springframework.test.context.jdbc.Sql;

@org.springframework.test.context.ActiveProfiles("datetime-test")
@Sql( "jpa_datetime_field_test.sql")
public class JpaDateTimeFieldTest extends AbstractDateTimeFieldTest {
}
