package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.lifecycle;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFieldValidationLifecycleTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JooqFieldValidationLifecycleTestApplication.class, properties = "spring.datasource.url=jdbc:sqlite::memory:")
@Sql(scripts = "lifecycle_validation_test.sql")
public class JooqFieldValidationLifecycleTest extends AbstractFieldValidationLifecycleTest {
}
