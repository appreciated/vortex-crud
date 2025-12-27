package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.select_field;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JpaSelectFieldValidationTestApplication.class)
@Sql(scripts = "select_field_validation_test.sql")
public class JpaSelectFieldValidationTest extends com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSelectFieldValidationTest {
}
