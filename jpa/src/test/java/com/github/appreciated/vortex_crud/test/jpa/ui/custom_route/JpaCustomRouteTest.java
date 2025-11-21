package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCustomRouteTest;

/**
 * JPA-specific test for CustomRoute functionality.
 * Tests that custom Vaadin @Route views can be integrated into VortexCrud menu system.
 */
public class JpaCustomRouteTest extends AbstractCustomRouteTest {

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
