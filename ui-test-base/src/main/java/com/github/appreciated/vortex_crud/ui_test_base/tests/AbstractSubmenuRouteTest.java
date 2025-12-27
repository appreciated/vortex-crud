package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;

/**
 * Base test for SubmenuRoute navigation.
 */
public abstract class AbstractSubmenuRouteTest extends BaseUITest {

    protected String getParentPath() {
        return "tasks";
    }

    protected String getChildLabel() {
        return "Open";
    }

    protected String getChildPath() {
        return "tasks/open";
    }

    @Test
    void testSubrouteNavigation() {
        navigateTo(getParentPath());
        waitForAnyElementContainingText(getChildLabel()).click();
        waitForUrlToBe(getChildPath());
    }
}
