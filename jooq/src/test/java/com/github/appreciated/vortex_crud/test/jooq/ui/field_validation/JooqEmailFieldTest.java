package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractEmailFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("seed_email.sql")
public class JooqEmailFieldTest extends AbstractEmailFieldTest {
    @Override
    public String getValidationPath() {
        return "email-validation-test";
    }
}
