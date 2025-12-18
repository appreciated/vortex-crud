package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractAdditionalFieldsLifecycleTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "additional_fields_test.sql")
public class JpaAdditionalFieldsLifecycleTest extends AbstractAdditionalFieldsLifecycleTest {
}
