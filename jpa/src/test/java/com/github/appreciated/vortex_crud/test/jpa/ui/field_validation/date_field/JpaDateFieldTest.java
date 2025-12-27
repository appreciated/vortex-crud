package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.date_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateFieldTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JpaDateFieldTestApplication.class)
@Sql(scripts = "date_field_validation_test.sql")
public class JpaDateFieldTest extends AbstractDateFieldTest {
}
