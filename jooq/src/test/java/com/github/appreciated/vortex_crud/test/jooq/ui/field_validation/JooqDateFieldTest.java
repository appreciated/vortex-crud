package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("jooq_date_field_test.sql")
public class JooqDateFieldTest extends AbstractDateFieldTest {
}
