package com.github.appreciated.vortex_crud.uitest.pages;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectCardsTest extends BaseUITest {

    @Test
    void checkIfListingVisible() {
        navigateTo("projects-cards");
        WebElement webElement = waitForAnyElementContainingText("Project Alpha");
        assertEquals("h4", webElement.getTagName());
    }

    @Test
    void checkIfNavigationPossible() {
        navigateTo("projects-cards");
        waitForAnyElementContainingText("Project Alpha").click();
        waitForUrlToBe("projects-cards/1");
        waitForElementWithTagAndValue("vaadin-text-field", "Project Alpha");
    }
}
