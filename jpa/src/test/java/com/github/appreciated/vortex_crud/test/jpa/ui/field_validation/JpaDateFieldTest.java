package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDateFieldTest;
import org.springframework.test.context.jdbc.Sql;

@org.springframework.test.context.ActiveProfiles("date-test")
@Sql( "jpa_date_field_test.sql")
public class JpaDateFieldTest extends AbstractDateFieldTest {
}
