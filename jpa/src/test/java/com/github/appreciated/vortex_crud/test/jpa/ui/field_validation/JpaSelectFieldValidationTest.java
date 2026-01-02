package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSelectFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql( "field_validation_test.sql")
public class JpaSelectFieldValidationTest extends AbstractSelectFieldTest {

    @Override
    public String getValidationPath() {
        return "select-field-test";
    }
}
