package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;

/**
 * Basic smoke test for verifying translations.
 */
public abstract class AbstractI18nSmokeTest extends BaseUITest {

    protected abstract String getPath();

    protected abstract String getEnglishText();

    protected abstract String getGermanText();

    @Test
    void testLanguageSwitch() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getEnglishText());
        navigateTo(getPath() + "?lang=de");
        waitForAnyElementContainingText(getGermanText());
    }
}
