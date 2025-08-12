package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractOneToManyTest extends BaseUITest {

    public String getPath() { return "one-to-many-test"; }

    @Test
    void testListingVisible() {
        navigateTo(getPath());
        WebElement element = waitForAnyElementContainingText("Parent A");
        // it should be inside a grid cell (like in validation test)
        assertEquals("vaadin-grid-cell-content", element.getTagName());
    }

    @Test
    void testOpenParentAndSeeChildren() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Parent A").click();
        waitForUrlToBe(getPath() + "/1");
        // Check that the child's names are visible on the page
        waitForAnyElementContainingText("Child A1");
        waitForAnyElementContainingText("Child A2");
    }
}
