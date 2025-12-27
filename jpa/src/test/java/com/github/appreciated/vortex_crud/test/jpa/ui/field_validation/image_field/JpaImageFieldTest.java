package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.image_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractImageFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "image_field_validation_test.sql")
public class JpaImageFieldTest extends AbstractImageFieldTest {
}
