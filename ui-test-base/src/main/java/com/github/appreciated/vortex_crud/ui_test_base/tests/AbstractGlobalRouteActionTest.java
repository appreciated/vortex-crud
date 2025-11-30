package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;

/**
 * Base test for global route actions.
 */
public abstract class AbstractGlobalRouteActionTest extends BaseUITest {

    protected String getPath() {
        return "missing-features-test";
    }

    @Test
    void testGlobalRouteActionVisible() {
        navigateTo(getPath());
        // "Print" button should be visible in the header or toolbar
        waitForAnyElementContainingText("Print");
    }
}
