package com.github.appreciated.vortex_crud.uitest.pages;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;

public class TasksMasterDetailTest extends BaseUITest {

    @Test
    void testNestedMasterDetailEntityNavigation() {
        navigateTo("tasks");

        waitForAnyElementContainingText("Abgeschlossen").click();
        waitForUrlToBe("tasks/done");

        waitForAnyElementContainingText("Design Homepage").click();
        waitForUrlToBe("tasks/done/1");

        waitForElementWithTagAndValue("vaadin-text-field", "Design Homepage");
        waitForElementWithTagAndValue("vaadin-text-area", "Create the design for the homepage of the web app");
        waitForElementWithTagAndValue("vaadin-select-item", "In Arbeit");
        waitForElementWithTagAndInputValue("vaadin-combo-box", "max@mustermann.de");
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-12-01");
        waitForElementContainingText("vaadin-card", "need to finalize the design by the end of the week.");

        waitForElementContainingText("vaadin-card", "Database Setup");
        waitForElementContainingText("vaadin-card", "User Authentication");
        waitForElementContainingText("vaadin-card", "Frontend Integration");
    }

    @Test
    void testNestedMasterDetailKanbanEntityNavigation() {
        navigateTo("tasks");

        waitForAnyElementContainingText("Offen").click();
        waitForUrlToBe("tasks/open");

        waitForAnyElementContainingText("Design Homepage").click();
        waitForUrlToBe("tasks/open/1");

        waitForElementWithTagAndValue("vaadin-text-field", "Design Homepage");
        waitForElementWithTagAndValue("vaadin-text-area", "Create the design for the homepage of the web app");
        waitForElementWithTagAndValue("vaadin-select-item", "In Arbeit");
        waitForElementWithTagAndInputValue("vaadin-combo-box", "max@mustermann.de");
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-12-01");
        waitForElementContainingText("vaadin-card", "We need to finalize the design by the end of the week.");

        waitForElementContainingText("vaadin-card", "Database Setup");
        waitForElementContainingText("vaadin-card", "User Authentication");
        waitForElementContainingText("vaadin-card", "Frontend Integration");
    }

}
