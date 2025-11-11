package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCustomRouteTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "custom_route_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaCustomRouteTest extends AbstractCustomRouteTest {

    @Override
    protected String getCustomRoutePath() {
        return "dashboard";
    }

    @Override
    protected String getCustomRouteMenuTitle() {
        return "Custom Dashboard";
    }

    @Override
    protected String getExpectedViewContent() {
        return "This is a custom view created with @Route annotation and integrated into VortexCrud menu";
    }
}
