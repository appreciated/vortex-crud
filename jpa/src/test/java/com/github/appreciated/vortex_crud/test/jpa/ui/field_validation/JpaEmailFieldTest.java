package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractEmailFieldTest;
import org.springframework.test.context.jdbc.Sql;

@org.springframework.test.context.ActiveProfiles("email-test")
@Sql( "jpa_email_field_test.sql")
public class JpaEmailFieldTest extends AbstractEmailFieldTest {
}
