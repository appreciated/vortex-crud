package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.lifecycle;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFieldValidationLifecycleTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JpaLifecycleFieldValidationTestApplication.class)
@Sql(scripts = "lifecycle_validation_test.sql")
public class JpaFieldValidationLifecycleTest extends AbstractFieldValidationLifecycleTest {
}
