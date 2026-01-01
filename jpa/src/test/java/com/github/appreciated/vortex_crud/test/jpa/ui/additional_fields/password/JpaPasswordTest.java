package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields.password;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractPasswordTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql( "password_test.sql")
public class JpaPasswordTest extends AbstractPasswordTest {
}
