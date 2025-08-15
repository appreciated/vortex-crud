package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base test for Master-Detail views.
 */
public abstract class AbstractMasterDetailTest extends BaseUITest {

    /**
     * @return the path to the Master-Detail view
     */
    protected String getPath() {
        return "tasks";
    }

    protected String getExistingItemName() {
        return "Done Task A";
    }

    @Test
    void testMasterDetailListingVisible() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName());
    }

    @Disabled //TODO Allow to create new entries in master-detail views
    @Test
    void testCreateEntry() {
        navigateTo(getPath());
        WebElement field = waitForElement(By.xpath("//vaadin-dialog-overlay//vaadin-text-field"))
                .findElement(By.tagName("input"));
        field.sendKeys("Created Entry");
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getPath());
        waitForAnyElementContainingText("Created Entry");
    }

    @Test
    void testUpdateEntry() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExistingItemName()).click();
        waitForUrlToBe(getPath() + "/1");
        WebElement field = waitForElement(By.tagName("vaadin-text-field"))
                .findElement(By.tagName("input"));
        field.clear();
        field.sendKeys("Updated Entry");
        waitForAnyElementContainingText("Save").click();
        waitForAnyElementContainingText("Entry successfully saved");
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
