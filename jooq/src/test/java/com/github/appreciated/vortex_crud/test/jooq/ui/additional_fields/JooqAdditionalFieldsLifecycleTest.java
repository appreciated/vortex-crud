package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractAdditionalFieldsLifecycleTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "additional_fields_test.sql")
public class JooqAdditionalFieldsLifecycleTest extends AbstractAdditionalFieldsLifecycleTest {
}
