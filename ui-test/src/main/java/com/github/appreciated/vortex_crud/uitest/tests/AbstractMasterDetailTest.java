package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;

/**
 * Base test for Master-Detail views.
 */
public abstract class AbstractMasterDetailTest extends BaseUITest {

    /**
     * @return the path to the Master-Detail view
     */
    protected abstract String getPath();

    /**
     * @return a column title expected in the master list
     */
    protected String getExpectedColumnTitle() {
        return "Title";
    }

    @Test
    void testMasterDetailListingVisible() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedColumnTitle());
    }
}
