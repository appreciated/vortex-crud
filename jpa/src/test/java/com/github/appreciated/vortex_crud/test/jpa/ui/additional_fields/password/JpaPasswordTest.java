package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields.password;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractPasswordTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "password_test.sql")
@SpringBootTest(classes = JpaPasswordTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JpaPasswordTest extends AbstractPasswordTest {
}
