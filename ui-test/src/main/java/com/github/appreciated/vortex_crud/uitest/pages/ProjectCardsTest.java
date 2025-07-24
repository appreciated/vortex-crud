package com.github.appreciated.vortex_crud.uitest.pages;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public class ProjectCardsTest extends BaseUITest {

    @Test
    void checkIfListingVisible() {
        navigateTo("");
        WebElement card = waitForElement(By.tagName("vaadin-card"));
        String text = card.getAttribute("innerText");
        Assertions.assertTrue(text.startsWith("Project Alpha"));
    }

    @Test
    void checkIfNavigationPossible() {
        navigateTo("");
        WebElement card = waitForElement(By.tagName("vaadin-text-field"));
        card.click();

        WebElement inputField = waitForElement(By.tagName("vaadin-text-field"));
        Assertions.assertTrue(inputField.getDomAttribute("value").startsWith("Project Alpha"));
    }
}
