package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Base test for custom data store implementations.
 * Tests that custom repositories (like FileSystemDataStore) work with the UI.
 *
 * Concrete implementations must provide test data through their specific mechanism
 * (e.g., create files for FileSystem, documents for MongoDB, etc.)
 */
public abstract class AbstractCustomDataStoreTest extends BaseUITest {

    /**
     * @return The route path for the custom data store view (e.g., "documents")
     */
    protected abstract String getPath();

    /**
     * @return A value that should be visible in the list view
     */
    protected abstract String getExpectedVisibleValue();

    /**
     * @return The ID of the entity for detail navigation test
     */
    protected String getDetailId() {
        return "1";
    }

    @Test
    void testCustomDataStoreListingVisible() {
        navigateTo(getPath());
        WebElement element = waitForAnyElementContainingText(getExpectedVisibleValue());
        assertEquals("vaadin-grid-cell-content", element.getTagName());
    }

    @Test
    void testNavigateToDetail() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue()).click();
        waitForUrlToBe(getPath() + "/" + getDetailId());
    }
}
