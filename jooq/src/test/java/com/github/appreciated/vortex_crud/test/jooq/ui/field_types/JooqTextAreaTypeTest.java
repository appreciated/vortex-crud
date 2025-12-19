package com.github.appreciated.vortex_crud.test.jooq.ui.field_types;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractTextAreaTypeTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "field_types_test.sql")
public class JooqTextAreaTypeTest extends AbstractTextAreaTypeTest {
}
