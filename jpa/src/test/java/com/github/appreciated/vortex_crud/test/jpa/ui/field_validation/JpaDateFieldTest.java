package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("seed_date.sql")
public class JpaDateFieldTest extends AbstractDateFieldTest {
    @Override
    public String getValidationPath() {
        return "date-validation-test";
    }
}
