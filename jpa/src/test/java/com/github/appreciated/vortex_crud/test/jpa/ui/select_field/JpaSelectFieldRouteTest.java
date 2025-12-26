package com.github.appreciated.vortex_crud.test.jpa.ui.select_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSelectFieldRouteTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("select_field_test.sql")
public class JpaSelectFieldRouteTest extends AbstractSelectFieldRouteTest {
}
