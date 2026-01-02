package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractTextFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("seed_text.sql")
public class JooqTextFieldTest extends AbstractTextFieldTest {
    @Override
    public String getValidationPath() {
        return "text-validation-test";
    }
}
