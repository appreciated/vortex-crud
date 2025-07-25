package com.github.appreciated.vortex_crud.uitest.pages;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TasksMasterDetailTest extends BaseUITest {

    @Test
    void checkDoneTaskNavigation() {
        navigateTo("tasks");

        waitForElement(By.xpath("//*[contains(text(), 'Abgeschlossen')]")).click();
        waitForUrlToBe("tasks/done");

        waitForElement(By.xpath("//*[contains(text(), 'Design Homepage')]")).click();
        waitForUrlToBe("tasks/done/1");

        WebElement inputField = waitForElement(By.tagName("vaadin-text-field"));
        String value = inputField.getAttribute("value");
        Assertions.assertEquals("Design Homepage", value);
    }

}
