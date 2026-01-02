package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCheckboxFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("jooq_checkbox_field_test.sql")
public class JooqCheckboxFieldTest extends AbstractCheckboxFieldTest {
}