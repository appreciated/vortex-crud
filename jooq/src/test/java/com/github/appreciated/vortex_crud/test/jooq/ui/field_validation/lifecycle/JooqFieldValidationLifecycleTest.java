package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.lifecycle;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFieldValidationLifecycleTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("lifecycle_validation_test.sql")
public class JooqFieldValidationLifecycleTest extends AbstractFieldValidationLifecycleTest {
}
