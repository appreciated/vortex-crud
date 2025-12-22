package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractNumberFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("field_validation_test.sql")
public class JooqNumberFieldTest extends AbstractNumberFieldTest {
}
