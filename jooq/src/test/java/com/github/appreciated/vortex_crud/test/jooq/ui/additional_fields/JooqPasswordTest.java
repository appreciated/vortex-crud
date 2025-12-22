package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractPasswordTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("password_test.sql")
public class JooqPasswordTest extends AbstractPasswordTest {
}
