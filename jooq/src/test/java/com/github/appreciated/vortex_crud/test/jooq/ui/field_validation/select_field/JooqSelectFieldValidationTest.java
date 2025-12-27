package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.select_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSelectFieldValidationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JooqSelectFieldValidationTestApplication.class, properties = "spring.datasource.url=jdbc:sqlite::memory:")
@Sql(scripts = "select_field_validation_test.sql")
public class JooqSelectFieldValidationTest extends AbstractSelectFieldValidationTest {
}
