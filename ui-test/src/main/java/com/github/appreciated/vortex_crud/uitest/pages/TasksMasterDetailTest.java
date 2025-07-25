package com.github.appreciated.vortex_crud.uitest.pages;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TasksMasterDetailTest extends BaseUITest {

    @Test
    void testNestedEntityNavigation() {
        navigateTo("tasks");

        waitForElementContainingText("Abgeschlossen").click();
        waitForUrlToBe("tasks/done");

        waitForElementContainingText("Design Homepage").click();
        waitForUrlToBe("tasks/done/1");

        Assertions.assertTrue(hasElementWithTagAndValue("vaadin-text-field", "Design Homepage"));
        Assertions.assertTrue(hasElementWithTagAndValue("vaadin-text-area", "Create the design for the homepage of the web app"));
        Assertions.assertTrue(hasElementWithTagAndValue("vaadin-combo-box", "In Arbeit"));
        Assertions.assertTrue(hasElementWithTagAndValue("vaadin-combo-box", "max@mustermann.de"));
        Assertions.assertTrue(hasElementWithTagAndValue("vaadin-date-picker", "1.12.2013"));
        Assertions.assertTrue(hasElementContainingText("We need to finalize the design by the end of the week."));
    }

}
