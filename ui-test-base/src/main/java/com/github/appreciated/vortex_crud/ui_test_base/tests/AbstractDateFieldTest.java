package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;

public abstract class AbstractDateFieldTest extends BaseUITest {

    public String getValidationPath() {
        return "field-validation-test";
    }

    @Test
    void testDateFieldLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-01-01");
    }
}
