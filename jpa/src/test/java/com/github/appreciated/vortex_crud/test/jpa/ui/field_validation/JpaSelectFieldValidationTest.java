package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSelectFieldTest;
import org.springframework.test.context.jdbc.Sql;

@org.springframework.test.context.ActiveProfiles("select-test")
@Sql( "jpa_select_field_test.sql")
public class JpaSelectFieldValidationTest extends AbstractSelectFieldTest {
}
