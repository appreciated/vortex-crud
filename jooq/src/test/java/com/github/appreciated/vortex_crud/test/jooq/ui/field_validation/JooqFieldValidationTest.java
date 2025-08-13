package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractFieldValidationTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "field_validation_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqFieldValidationTest extends AbstractFieldValidationTest {
}