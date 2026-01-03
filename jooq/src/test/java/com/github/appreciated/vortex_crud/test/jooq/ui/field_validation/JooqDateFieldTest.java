package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("date_field_validation_test.sql")
public class JooqDateFieldTest extends AbstractDateFieldTest {
}
