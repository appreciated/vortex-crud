package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract test template for testing CustomRoute functionality.
 * This verifies that custom Vaadin views annotated with @Route can be
 * integrated into the VortexCrud menu system.
 */
public abstract class AbstractCustomRouteTest extends BaseUITest {

    /**
     * @return The path configured in the routes map (e.g., "dashboard")
     */
    protected abstract String getCustomRoutePath();

    /**
     * @return The translation key for the menu title (e.g., "route.dashboard.title")
     */
    protected abstract String getCustomRouteMenuTitle();

    /**
     * @return Text content that should be visible in the custom view
     */
    protected abstract String getExpectedViewContent();

    /**
     * Test that the custom route appears in the menu.
     */
    @Test
    void testCustomRouteInMenu() {
        navigateTo("");
        waitForAnyElementContainingText(getCustomRouteMenuTitle());
    }

    /**
     * Test that clicking the menu item navigates to the custom view.
     */
    @Test
    void testNavigateToCustomRoute() {
        navigateTo("");
        waitForAnyElementContainingText(getCustomRouteMenuTitle()).click();
        waitForUrlToBe(getCustomRoutePath());
    }

    /**
     * Test that the custom view content is rendered correctly.
     */
    @Test
    void testCustomViewContent() {
        navigateTo(getCustomRoutePath());
        waitForAnyElementContainingText(getExpectedViewContent());
    }

    /**
     * Test that direct navigation to the custom route works.
     */
    @Test
    void testDirectNavigation() {
        navigateTo(getCustomRoutePath());
        waitForUrlToBe(getCustomRoutePath());

        // Verify the view is rendered
        waitForAnyElementContainingText(getExpectedViewContent());
    }

    /**
     * Test that the custom route can access its own path and display it.
     */
    @Test
    void testCustomRouteCanAccessPath() {
        navigateTo(getCustomRoutePath());

        // Verify URL is correct
        assertTrue(driver.getCurrentUrl().contains(getCustomRoutePath()),
                "URL should contain the custom route path");
    }
}
