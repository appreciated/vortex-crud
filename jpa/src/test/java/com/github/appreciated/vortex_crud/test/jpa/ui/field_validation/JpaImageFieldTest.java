package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractImageFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("seed_image.sql")
public class JpaImageFieldTest extends AbstractImageFieldTest {
    @Override
    public String getValidationPath() {
        return "image-validation-test";
    }
}
