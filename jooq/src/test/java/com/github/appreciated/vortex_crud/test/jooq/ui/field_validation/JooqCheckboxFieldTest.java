package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCheckboxFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("seed_checkbox.sql")
public class JooqCheckboxFieldTest extends AbstractCheckboxFieldTest {
    @Override
    public String getValidationPath() {
        return "checkbox-validation-test";
    }
}
