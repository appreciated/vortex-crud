package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateTimeFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql( "jpa_datetime_field_test.sql")
public class JpaDateTimeFieldTest extends AbstractDateTimeFieldTest {
}
