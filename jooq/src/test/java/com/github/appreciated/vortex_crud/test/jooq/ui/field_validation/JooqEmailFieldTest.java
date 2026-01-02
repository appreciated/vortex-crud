package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractEmailFieldTest;
import org.springframework.test.context.jdbc.Sql;

@org.springframework.test.context.ActiveProfiles("email-test")
@Sql("jooq_email_field_test.sql")
public class JooqEmailFieldTest extends AbstractEmailFieldTest {
}
