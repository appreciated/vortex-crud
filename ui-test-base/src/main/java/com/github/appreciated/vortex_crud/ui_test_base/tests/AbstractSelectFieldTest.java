package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;

public abstract class AbstractSelectFieldTest extends BaseUITest {

    public String getValidationPath() {
        return "select-field-validation-test";
    }

    @Test
    void testSelectFieldLoading() {
        navigateTo(getValidationPath());
        waitForAnyElementContainingText("Test Value").click();
        waitForUrlToBe(getValidationPath() + "/1");
        waitForElementWithTagAndValue("vaadin-select", "1");
    }
}
