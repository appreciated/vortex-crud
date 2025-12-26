package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class AbstractSingleComponentRouteTest extends BaseUITest {

    protected String getRoutePath() {
        return "single-component-test";
    }

    @Test
    public void testReadAndEditMode() {
        navigateTo(getRoutePath());

        // Verify Read Mode
        Locator editButton = page.locator("vaadin-button").filter(new Locator.FilterOptions().setHas(page.locator("vaadin-icon[icon='vaadin:edit']")));
        assertThat(editButton).isVisible();
        
        // Verify Content is visible (Markdown/Hugerte content)
        // Hugerte uses vaadin-huge-rte component
        Locator hugeRte = page.locator("vaadin-huge-rte");
        assertThat(hugeRte).isVisible();
        
        // Click Edit
        editButton.click();
        
        // Verify Edit Mode
        Locator saveButton = page.locator("vaadin-button").filter(new Locator.FilterOptions().setHas(page.locator("vaadin-icon[icon='vaadin:check']")));
        Locator cancelButton = page.locator("vaadin-button").filter(new Locator.FilterOptions().setHas(page.locator("vaadin-icon[icon='vaadin:close']")));
        
        assertThat(saveButton).isVisible();
        assertThat(cancelButton).isVisible();
        assertThat(editButton).not().isVisible();
        
        // Let's try to click cancel and verify we go back to read mode.
        cancelButton.click();
        assertThat(editButton).isVisible();
        assertThat(saveButton).not().isVisible();
    }
}
