package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractAdditionalFieldsTextAreaTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "textarea_test.sql")
public class JooqAdditionalFieldsTextAreaTest extends AbstractAdditionalFieldsTextAreaTest {
}
