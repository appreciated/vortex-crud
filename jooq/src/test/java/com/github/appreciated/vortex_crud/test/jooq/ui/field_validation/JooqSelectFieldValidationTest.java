package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSelectFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("select_field_test.sql")
public class JooqSelectFieldValidationTest extends AbstractSelectFieldTest {

    @Override
    public String getValidationPath() {
        return "select-field-test";
    }
}
