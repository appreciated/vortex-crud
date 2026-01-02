package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFieldValidationLifecycleTest;
import org.springframework.test.context.jdbc.Sql;

@org.springframework.test.context.ActiveProfiles("lifecycle-test")
@Sql( "jpa_lifecycle_field_test.sql")
public class JpaFieldValidationLifecycleTest extends AbstractFieldValidationLifecycleTest {
}
