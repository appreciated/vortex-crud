package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractOneToManyTest extends BaseUITest {

    public String getPath() {
        return "one-to-many-test";
    }

    protected String getExistingParentName() {
        return "Parent A";
    }

    @Test
    void testListingVisible() {
        navigateTo(getPath());
        WebElement element = waitForAnyElementContainingText(getExistingParentName());
        // it should be inside a grid cell (like in validation test)
        assertEquals("vaadin-grid-cell-content", element.getTagName());
    }

    @Test
    void testOpenParentAndSeeChildren() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingParentName()).click();
        waitForUrlToBe(getPath() + "/1");
        // Check that the child's names are visible on the page
        waitForAnyElementContainingText("Child A1");
        waitForAnyElementContainingText("Child A2");
    }

    @Test
    void testCreateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Create").click();
        WebElement field = waitForElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        field.sendKeys("Created Parent");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Created Parent");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingParentName()).click();
        waitForUrlToBe(getPath() + "/1");
        WebElement field = waitForElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        field.clear();
        field.sendKeys("Updated Parent");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Updated Parent");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingParentName()).click();
        waitForUrlToBe(getPath() + "/1");
        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getPath());
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + getExistingParentName() + "')]"));
        assertTrue(elements.stream().noneMatch(WebElement::isDisplayed));
    }
}
