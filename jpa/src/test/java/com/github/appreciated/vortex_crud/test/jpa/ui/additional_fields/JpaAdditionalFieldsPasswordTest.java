package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractAdditionalFieldsPasswordTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "password_test.sql")
public class JpaAdditionalFieldsPasswordTest extends AbstractAdditionalFieldsPasswordTest {
}
