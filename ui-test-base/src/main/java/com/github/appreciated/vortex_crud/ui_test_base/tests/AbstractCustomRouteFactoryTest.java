package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Base test for CustomRouteFactory functionality.
 * Tests that the factory renders an error message when CustomRoute is used in a context
 * where it falls back to the factory (e.g. nested in a SubmenuRoute) instead of standard Vaadin routing.
 */
public abstract class AbstractCustomRouteFactoryTest extends BaseUITest {

    /**
     * @return The path to the nested custom route (e.g., "submenu/custom-nested")
     */
    protected abstract String getNestedCustomRoutePath();

    /**
     * Test that CustomRouteFactory renders error message when accessed via internal routing
     */
    @Test
    void testCustomRouteFactoryErrorMessage() {
        navigateTo(getNestedCustomRoutePath());

        // Verify the error message from CustomRouteFactory is displayed
        Locator errorText = waitForAnyElementContainingText("CustomRoute misconfigured - check @Route annotation and path match");
        assertThat(errorText).isVisible();
    }
}
