package com.github.appreciated.vortex_crud.uitest.pages;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectGridTest extends BaseUITest {

    @Test
    void checkIfListingVisible() {
        navigateTo("projects-list");
        WebElement webElement = waitForAnyElementContainingText("Project Alpha");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void checkIfNavigationPossible() {
        navigateTo("projects-list");
        waitForAnyElementContainingText("Project Alpha").click();
        waitForUrlToBe("projects-list/1");
        waitForElementWithTagAndValue("vaadin-text-field", "Project Alpha");
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-01-01");
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-12-31");
    }
}
