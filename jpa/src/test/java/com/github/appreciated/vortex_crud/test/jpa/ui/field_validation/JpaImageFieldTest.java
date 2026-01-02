package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractImageFieldTest;
import org.springframework.test.context.jdbc.Sql;

@org.springframework.test.context.ActiveProfiles("image-test")
@Sql( "jpa_image_field_test.sql")
public class JpaImageFieldTest extends AbstractImageFieldTest {
}
