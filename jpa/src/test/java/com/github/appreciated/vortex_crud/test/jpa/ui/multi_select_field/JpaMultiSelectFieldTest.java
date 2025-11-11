package com.github.appreciated.vortex_crud.test.jpa.ui.multi_select_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMultiSelectFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "multi_select_field_test.sql")
public class JpaMultiSelectFieldTest extends AbstractMultiSelectFieldTest {
}
