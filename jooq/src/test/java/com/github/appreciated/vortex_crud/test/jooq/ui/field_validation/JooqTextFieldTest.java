package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractTextFieldTest;
import org.springframework.test.context.jdbc.Sql;

@org.springframework.test.context.ActiveProfiles("text-test")
@Sql("jooq_text_field_test.sql")
public class JooqTextFieldTest extends AbstractTextFieldTest {
}
