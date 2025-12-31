package com.github.appreciated.vortex_crud.test.jooq.ui.search_route;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSearchRouteTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = JooqSearchRouteTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jooq/ui/search_route/search_route_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqSearchRouteTest extends AbstractSearchRouteTest {
}
