package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql( "date_field_validation_test.sql")
public class JpaDateFieldTest extends AbstractDateFieldTest {
}
