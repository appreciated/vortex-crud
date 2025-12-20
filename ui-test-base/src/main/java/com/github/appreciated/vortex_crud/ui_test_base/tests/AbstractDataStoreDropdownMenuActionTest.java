package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Base test for DataStore Dropdown Menu Action.
 */
public abstract class AbstractDataStoreDropdownMenuActionTest extends BaseUITest {

    @Test
    void testDataStoreDropdownMenuAction() {
        navigateTo(""); // Navigate to root

        // 1. Verify Dropdown is visible
        // Factory uses vaadin-select
        Locator select = page.locator("vaadin-select").filter(new Locator.FilterOptions().setHasText("Active Project"));
        // Or just by label if it's set on the component

        assertThat(select).isVisible();

        // 2. Select an item
        // With vaadin-select, items are in an overlay.
        // We can set the value via JS or click.

        // Execute JS to open and select to be robust, or try UI interaction.
        // BaseUITest has helpers?

        // Let's try UI interaction.
        select.click();

        // Items are in vaadin-select-overlay -> vaadin-list-box -> vaadin-item
        Locator item = page.locator("vaadin-item >> text=Project A");
        assertThat(item).isVisible();
        item.click();

        // Verify selection
        // The value should be updated.
        assertThat(select).containsText("Project A");

        // 3. Verify action execution (if any side effect is observable)
        // The action just sets the value.
        // Maybe we can check if a notification appeared if we attached a listener (not standard in config).
        // The test mainly verifies it renders and has data.
    }
}
