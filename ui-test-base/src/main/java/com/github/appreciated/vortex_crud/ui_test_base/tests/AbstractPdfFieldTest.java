package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;

public abstract class AbstractPdfFieldTest extends BaseUITest {

    protected String getPath() {
        return "missing-features-test";
    }

    @Test
    void testPdfField() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Test Entity 1").click();
        waitForUrlToBe(getPath() + "/1");

        // Check PdfField
        waitForAnyElementContainingText("PDF");
    }
}
