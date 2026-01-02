package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractImageFieldTest;
import org.springframework.test.context.jdbc.Sql;

@org.springframework.test.context.ActiveProfiles("image-test")
@Sql("jooq_image_field_test.sql")
public class JooqImageFieldTest extends AbstractImageFieldTest {
}
