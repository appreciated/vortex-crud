package com.github.appreciated.vortex_crud.test.jpa.ui.multi_form;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMultiFormRouteTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "multi_form_test.sql")
public class JpaMultiFormTest extends AbstractMultiFormRouteTest {
}
