package com.github.appreciated.vortex_crud.uitest.pages;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TasksMasterDetailTest extends BaseUITest {

    @Test
    void testNestedEntityNavigation() {
        navigateTo("tasks");

        waitForElement(By.xpath("//*[contains(text(), 'Abgeschlossen')]")).click();
        waitForUrlToBe("tasks/done");

        waitForElement(By.xpath("//*[contains(text(), 'Design Homepage')]")).click();
        waitForUrlToBe("tasks/done/1");

        List<WebElement> inputFields = waitForElements(By.tagName("vaadin-text-field"));
        Assertions.assertTrue(
                inputFields.stream()
                        .map(e -> e.getAttribute("value"))
                        .anyMatch(s -> s != null && s.startsWith("Design Homepage")),
                "Expected at least one field value to start with 'Design Homepage'"
        );
    }

}
