package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractPasswordTest extends BaseUITest {

    public String getAdditionalFieldsPath() {
        return "password-test";
    }

    /**
     * Override this method to provide the expected entity name from test data.
     * Default is "Password Test Entity".
     */
    protected String getExpectedEntityName() {
        return "Password Test Entity";
    }

    @Test
    void testPasswordLoading() {
        navigateTo(getAdditionalFieldsPath());
        page.getByText(getExpectedEntityName(), new Page.GetByTextOptions().setExact(true)).first().click();
        waitForUrlToBe(getAdditionalFieldsPath() + "/1");

        // Verify Password field exists (value should be masked)
        Locator passwordField = waitForElement("vaadin-password-field").locator("input");
        assertEquals("password", passwordField.getAttribute("type"));
    }

    @Test
    void testPasswordFieldMasking() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Create").click();

        // Wait for and find password field
        Locator passwordField = waitForElement("vaadin-password-field").locator("input");

        // Verify it's a password type input (masked)
        assertEquals("password", passwordField.getAttribute("type"));

        // Enter a password
        passwordField.fill("MySecretPassword");

        // The value should be present but masked in the UI
        assertEquals("MySecretPassword", passwordField.inputValue());
    }
}
