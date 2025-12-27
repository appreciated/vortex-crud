package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.lifecycle;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFieldValidationLifecycleTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "lifecycle_validation_test.sql")
public class JpaFieldValidationLifecycleTest extends AbstractFieldValidationLifecycleTest {
}
