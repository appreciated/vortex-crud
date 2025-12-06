package com.github.appreciated.vortex_crud.test.jpa.ui.global_route_action;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractGlobalRouteActionTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "global_route_action_test.sql")
public class JpaGlobalRouteActionTest extends AbstractGlobalRouteActionTest {
}
