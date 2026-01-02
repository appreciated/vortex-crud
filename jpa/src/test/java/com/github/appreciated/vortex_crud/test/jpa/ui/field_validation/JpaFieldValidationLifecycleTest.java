package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFieldValidationLifecycleTest;
import org.springframework.test.context.jdbc.Sql;

@Sql( "jpa_lifecycle_field_test.sql")
public class JpaFieldValidationLifecycleTest extends AbstractFieldValidationLifecycleTest {
}
