package com.github.appreciated.vortex_crud.test.jooq.ui.global_route_action;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractGlobalRouteActionTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = JooqGlobalRouteActionTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "vaadin.productionMode=true")
@Sql(scripts = "global_route_action_test.sql")
public class JooqGlobalRouteActionTest extends AbstractGlobalRouteActionTest {
}
