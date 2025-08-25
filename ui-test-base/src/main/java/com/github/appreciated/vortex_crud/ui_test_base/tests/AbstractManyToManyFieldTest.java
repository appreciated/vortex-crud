package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractManyToManyFieldTest extends BaseUITest {

    public String getPath() {
        return "many-to-many-test";
    }

    protected String getExistingItemName() {
        return "Item 1";
    }

    @Test
    void testListingVisible() {
        navigateTo(getPath());
        WebElement element = waitForAnyElementContainingText(getExistingItemName());
        assertEquals("vaadin-grid-cell-content", element.getTagName());
    }

    @Test
    void testOpenItemAndSeeRelated() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getPath() + "/1");
        // Related entries seeded: Item 2, Item 3
        waitForAnyElementContainingText("Item 2");
        waitForAnyElementContainingText("Item 3");
    }

    @Test
    void testAddEntryToChildCollection() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Item 2").click();
        waitForUrlToBe(getPath() + "/2");
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), 'Item 3')]"));
        assertTrue(elements.stream().noneMatch(WebElement::isDisplayed));
        waitForElement(By.xpath("//vaadin-icon[@icon='vaadin:plus']/.."))
                .click();
        waitForAnyElementContainingText("Item 3").click();
        waitForAnyElementContainingText("Link").click();
        waitForAnyElementContainingText("Item 3");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getPath() + "/1");
        WebElement field = waitForElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        field.clear();
        field.sendKeys("Updated Item");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Updated Item");
    }

    @Test
    void testDeleteEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getPath() + "/1");
        waitForAnyElementContainingText("Delete").click();
        waitForUrlToBe(getPath());
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + getExistingItemName() + "')]"));
        assertTrue(elements.stream().noneMatch(WebElement::isDisplayed));
    }
}
