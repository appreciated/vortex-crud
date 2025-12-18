package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractAdditionalFieldsTextAreaTest extends BaseUITest {

    public String getAdditionalFieldsPath() {
        return "additional-fields-test";
    }

    @Test
    void testTextAreaLoading() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
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

        // Fill Password field
        Locator passwordField = waitForElement("vaadin-password-field").locator("input");
        passwordField.fill("SecurePassword123");

        // Save
        waitForAnyElementContainingText("Save").click();
        waitForUrlToBe(getAdditionalFieldsPath());

        // Verify the entity was created
        waitForAnyElementContainingText("TextArea Test");
    }
}
