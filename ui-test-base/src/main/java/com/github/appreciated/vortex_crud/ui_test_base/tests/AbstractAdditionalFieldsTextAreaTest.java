package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractAdditionalFieldsTextAreaTest extends BaseUITest {

    public String getAdditionalFieldsPath() {
        return "textarea-test";
    }

    /**
     * Override this method to provide the expected entity name from test data.
     * Default is "TextArea Test Entity".
     */
    protected String getExpectedEntityName() {
        return "TextArea Test Entity";
    }

    @Test
    void testTextAreaLoading() {
        navigateTo(getAdditionalFieldsPath());
        page.getByText(getExpectedEntityName(), new Page.GetByTextOptions().setExact(true)).first().click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        // Verify TextArea field loads
        Locator textAreaField = waitForElement("vaadin-text-area").locator("textarea");
        assertTrue(textAreaField.inputValue().contains("This is a long description"));
    }

    @Test
    void testTextAreaInput() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        // Fill in required fields
        Locator nameField = waitForElementContainingText("vaadin-text-field", "Name").locator("input");
        nameField.fill("TextArea Test");

        // Fill in TextArea field
        Locator textAreaField = waitForElement("vaadin-text-area").locator("textarea");
        textAreaField.fill("This is a multi-line\ntext area\nwith several lines");

        // Save
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());

        // Verify the entity was created
        waitForAnyElementContainingText("TextArea Test");
    }
}
