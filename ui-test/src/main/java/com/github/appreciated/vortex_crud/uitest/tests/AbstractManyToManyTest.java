package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractManyToManyTest extends BaseUITest {

    public String getPath() {
        return "many-to-many-test";
    }

    @Test
    void testListingVisible() {
        navigateTo(getPath());
        WebElement element = waitForAnyElementContainingText("Item 1");
        assertEquals("vaadin-grid-cell-content", element.getTagName());
    }

    @Test
    void testOpenItemAndSeeRelated() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Item 1").click();
        waitForUrlToBe(getPath() + "/1");
        // Related entries seeded: Item 2, Item 3
        waitForAnyElementContainingText("Item 2");
        waitForAnyElementContainingText("Item 3");
    }
}
