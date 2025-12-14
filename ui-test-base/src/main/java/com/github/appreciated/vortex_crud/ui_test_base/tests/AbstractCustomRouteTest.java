package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Base test for CustomRoute functionality.
 * Tests that custom Vaadin @Route views can be integrated into VortexCrud menu system.
 */
public abstract class AbstractCustomRouteTest extends BaseUITest {

    /**
     * @return The path to the custom route (e.g., "dashboard")
     */
    protected String getCustomRoutePath() {
        return "dashboard";
    }

    /**
     * @return The menu label for the custom route (from i18n)
     */
    protected String getCustomRouteMenuLabel() {
        return "Custom Dashboard";
    }

    /**
     * @return Text expected to be visible in the custom view
     */
    protected String getExpectedCustomViewContent() {
        return "This is a custom dashboard";
    }

    /**
     * Test that custom route appears in menu and can be navigated to
     */
    @Test
    void testCustomRouteInMenu() {
        // Navigate to a different route first
        navigateTo("");

        // Find and click the custom route menu item
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Custom Dashboard")).click();

        // Verify navigation to custom route
        waitForUrlToBe(getCustomRoutePath());
    }

    /**
     * Test that custom view content is displayed
     */
    @Test
    void testCustomViewContentDisplayed() {
        navigateTo(getCustomRoutePath());

        // Verify custom view content is visible
        Locator content = waitForAnyElementContainingText(getExpectedCustomViewContent());
        assertThat(content).isVisible();
    }

    /**
     * Test that VortexCrud menu is visible when navigating to custom route
     * (verifies ProxyRouterLayout integration)
     */
    @Test
    void testMenuVisibleInCustomRoute() {
        navigateTo(getCustomRoutePath());

        // Menu should be visible - check for app-layout or menu container
        Locator appLayout = waitForElement("vaadin-app-layout");
        assertThat(appLayout).isVisible();
    }

    /**
     * Test direct navigation to custom route via URL
     */
    @Test
    void testDirectNavigationToCustomRoute() {
        navigateTo(getCustomRoutePath());

        // Verify we're on the right path
        waitForUrlToBe(getCustomRoutePath());

        // Verify content is displayed
        Locator content = waitForAnyElementContainingText(getExpectedCustomViewContent());
        assertThat(content).isVisible();
    }

    /**
     * Test navigation from custom route back to VortexCrud managed route
     */
    @Test
    void testNavigationFromCustomRoute() {
        // Navigate to custom route first
        navigateTo(getCustomRoutePath());
        waitForAnyElementContainingText(getExpectedCustomViewContent());

        // Navigate to home/default route
        navigateTo("");

        // Menu should still be visible
        Locator appLayout = waitForElement("vaadin-app-layout");
        assertThat(appLayout).isVisible();
    }
}
