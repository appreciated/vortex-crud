package com.github.appreciated.vortex_crud.uitest.pages;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectGridTest extends BaseUITest {

    @Test
    void checkIfListingVisible() {
        navigateTo("projects-list");
        WebElement webElement = waitForElement(By.xpath("//*[contains(text(), 'Project Alpha')]"));
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void checkIfNavigationPossible() {
        navigateTo("projects-list");
        waitForElement(By.xpath("//*[contains(text(), 'Project Alpha')]")).click();
        waitForUrlToBe("projects-list/1");
        WebElement inputField = waitForElement(By.tagName("vaadin-text-field"));
        String value = inputField.getAttribute("value");
        Assertions.assertTrue(value.startsWith("Project Alpha"));
    }
}
