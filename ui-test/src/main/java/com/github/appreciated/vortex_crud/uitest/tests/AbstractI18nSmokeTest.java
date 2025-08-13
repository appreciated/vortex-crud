package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;

/**
 * Basic smoke test for verifying translations.
 */
public abstract class AbstractI18nSmokeTest extends BaseUITest {

    protected String getPath() {
        return "images-list";
    }


    protected String getSampleDataText() {
        return "Images as list";
    }

    @Test
    void testLanguageSwitch() {
        navigateTo(getPath());
        // Verify that user provided data is visible and in the original language
        waitForAnyElementContainingText(getSampleDataText());
        // Create dialog should show a "Create" button which opens a dialog
        waitForAnyElementContainingText("Create").click();
        // The dialog should contain a "Save" button in English
        waitForAnyElementContainingText("Save");

        navigateTo(getPath() + "?lang=de");
        // User provided data should still not be translated
        waitForAnyElementContainingText(getSampleDataText());
        // Verify translated buttons
        waitForAnyElementContainingText("Erstellen").click();
        waitForAnyElementContainingText("Speichern");
    }
}
