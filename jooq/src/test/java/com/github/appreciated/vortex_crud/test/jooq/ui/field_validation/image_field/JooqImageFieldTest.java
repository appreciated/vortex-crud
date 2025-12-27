package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.image_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractImageFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("image_field_validation_test.sql")
public class JooqImageFieldTest extends AbstractImageFieldTest {
}
