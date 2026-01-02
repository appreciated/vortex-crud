package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFieldValidationLifecycleTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("seed_lifecycle.sql")
public class JooqFieldValidationLifecycleTest extends AbstractFieldValidationLifecycleTest {
    @Override
    public String getValidationPath() {
        return "lifecycle-validation-test";
    }
}
