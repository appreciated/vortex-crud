package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;

/**
 * Base test for subroute navigation.
 */
public abstract class AbstractSubrouteTest extends BaseUITest {

    /**
     * @return the parent path containing the subroutes
     */
    protected abstract String getParentPath();

    /**
     * @return the visible label of the subroute to click
     */
    protected abstract String getChildLabel();

    /**
     * @return the expected path after navigation
     */
    protected abstract String getChildPath();

    @Test
    void testSubrouteNavigation() {
        navigateTo(getParentPath());
        waitForAnyElementContainingText(getChildLabel()).click();
        waitForUrlToBe(getChildPath());
    }
}
