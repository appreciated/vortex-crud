package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.date_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateFieldTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JooqDateFieldTestApplication.class, properties = "spring.datasource.url=jdbc:sqlite::memory:")
@Sql(scripts = "date_field_validation_test.sql")
public class JooqDateFieldTest extends AbstractDateFieldTest {
}
