package com.github.appreciated.vortex_crud.test.jpa.ui.submenu_route;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSubmenuRouteTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "subroute_test.sql")
public class JpaSubmenuRouteTest extends AbstractSubmenuRouteTest {

}
