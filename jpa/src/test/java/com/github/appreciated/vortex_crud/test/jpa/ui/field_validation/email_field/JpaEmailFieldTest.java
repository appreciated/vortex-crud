package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.email_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractEmailFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "email_field_validation_test.sql")
public class JpaEmailFieldTest extends AbstractEmailFieldTest {
}
