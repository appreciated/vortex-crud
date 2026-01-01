package com.github.appreciated.vortex_crud.test.jpa.ui.search_route;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSearchRouteTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("search_route_test.sql")
public class JpaSearchRouteTest extends AbstractSearchRouteTest {
}
