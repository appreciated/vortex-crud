package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.datetime_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateTimeFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("datetime_field_validation_test.sql")
public class JooqDateTimeFieldTest extends AbstractDateTimeFieldTest {
}
