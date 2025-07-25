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

        Assertions.assertTrue(hasWebElementWithTagAndValue("vaadin-text-field", "Design Homepage"));
        Assertions.assertTrue(hasWebElementWithTagAndValue("vaadin-text-field", "Create the design for the homepage of the web app"));
        Assertions.assertTrue(hasWebElementWithTagAndValue("vaadin-combo-box", "Design Homepage"));
    }

    private boolean hasWebElementWithTagAndValue(String tagName, String value) {
        List<WebElement> webElements = waitForElements(By.tagName(tagName));
        return webElements.stream()
                .map(e -> e.getAttribute("value"))
                .anyMatch(s -> s != null && s.startsWith(value));
    }

}
