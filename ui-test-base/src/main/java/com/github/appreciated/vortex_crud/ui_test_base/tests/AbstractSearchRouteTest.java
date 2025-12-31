package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Base test for Search Route / Global Search functionality.
 */
public abstract class AbstractSearchRouteTest extends BaseUITest {

    @Test
    void testGlobalSearch() {
        navigateTo(""); // Navigate to root or dashboard

        // 1. Verify Search Bar is visible
        Locator searchComboBox = page.locator("vaadin-combo-box[placeholder='Search...']");
        assertThat(searchComboBox).isVisible();

        // 2. Perform a search
        // We assume there is data seeded (e.g., "Test Item") that matches the query.
        String query = "Test";
        // For Vaadin ComboBox, we need to access the internal input element
        Locator input = searchComboBox.locator("input");
        input.fill(query);

        // 3. Verify results appear in the dropdown
        // Vaadin ComboBox overlay
        Locator overlay = page.locator("vaadin-combo-box-overlay");
        assertThat(overlay).isVisible();

        Locator item = page.getByText("Grid > Test Item");
        assertThat(item).isVisible();

        // 4. Select a result and verify navigation
        item.click();

        // After selection, it should navigate to the item's detail view (e.g. form)
        // We can check if the URL changed or if the form is visible.
        // Assuming the test data navigates to a form for the item.
        assertThat(page.locator("vaadin-form-layout")).isVisible();
    }
}
