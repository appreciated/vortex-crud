package com.github.appreciated.vortex_crud.test.jooq.ui.custom_route;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCustomRouteTest;

public class JooqCustomRouteTest extends AbstractCustomRouteTest {

    @Override
    protected String getCustomRoutePath() {
        return "dashboard";
    }

    @Override
    protected String getCustomRouteMenuLabel() {
        return "Custom Dashboard";
    }

    @Override
    protected String getExpectedCustomViewContent() {
        return "This is a custom dashboard";
    }
}
