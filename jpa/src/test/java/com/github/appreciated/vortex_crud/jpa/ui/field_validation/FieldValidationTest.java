package com.github.appreciated.vortex_crud.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.jpa.ui.TestApplication;
import com.github.appreciated.vortex_crud.uitest.tests.AbstractFieldValidationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApplication.class)
@Sql(scripts = "field_validation_test.sql")
public class FieldValidationTest extends AbstractFieldValidationTest {
}