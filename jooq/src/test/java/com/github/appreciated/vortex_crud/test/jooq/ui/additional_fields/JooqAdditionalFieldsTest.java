package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractAdditionalFieldsTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "additional_fields_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqAdditionalFieldsTest extends AbstractAdditionalFieldsTest {
}
