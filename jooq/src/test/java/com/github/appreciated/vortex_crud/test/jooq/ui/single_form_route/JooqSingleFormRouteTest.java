package com.github.appreciated.vortex_crud.test.jooq.ui.single_form_route;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSingleFormRouteTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = JooqSingleFormRouteTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "vaadin.productionMode=true")
@Sql(scripts = "single_form_route_test.sql")
public class JooqSingleFormRouteTest extends AbstractSingleFormRouteTest {
}
