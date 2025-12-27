package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.checkbox_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCheckboxFieldTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JooqCheckboxFieldTestApplication.class, properties = "spring.datasource.url=jdbc:sqlite::memory:")
@Sql(scripts = "checkbox_field_validation_test.sql")
public class JooqCheckboxFieldTest extends AbstractCheckboxFieldTest {
}
