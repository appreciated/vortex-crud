package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.number_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractNumberFieldTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JpaNumberFieldTestApplication.class)
@Sql(scripts = "number_field_validation_test.sql")
public class JpaNumberFieldTest extends AbstractNumberFieldTest {
}
