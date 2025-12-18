package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractAdditionalFieldsPasswordTest extends BaseUITest {

    public String getAdditionalFieldsPath() {
        return "additional-fields-test";
    }

    @Test
    void testPasswordLoading() {
        navigateTo(getAdditionalFieldsPath());
        waitForAnyElementContainingText("Test Entity").click();
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
