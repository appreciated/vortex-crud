package com.github.appreciated.vortex_crud.uitest.pages;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ProjectCardsTest extends BaseUITest {

    @Test
    void testListing() {
        navigateTo("project-cards");
        WebElement webElement = waitForElement(By.cssSelector(".vaadin-card"));
        Assertions.assertTrue(webElement.getText().startsWith("Project Alpha1"));
    }
}
