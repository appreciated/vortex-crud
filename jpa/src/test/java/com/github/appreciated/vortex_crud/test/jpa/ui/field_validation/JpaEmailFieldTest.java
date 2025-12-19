package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractEmailFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "field_validation_test.sql")
public class JpaEmailFieldTest extends AbstractEmailFieldTest {
}
