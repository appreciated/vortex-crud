package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.Test;

public abstract class AbstractMissingFeaturesFieldsTest extends BaseUITest {

    protected abstract String getPath();

    @Test
    void testEntityLoadingAndFields() {
        navigateTo(getPath());
        waitForAnyElementContainingText("Test Entity 1").click();
        waitForUrlToBe(getPath() + "/1");

        // ReferenceField (ManyToOne) - Should show "Ref 1"
        waitForAnyElementContainingText("Ref 1");

        // MultiSelectField (ManyToMany) - Should show "Ref 1", "Ref 2"
        waitForAnyElementContainingText("Ref 1");
        waitForAnyElementContainingText("Ref 2");

        // Markdown - Check for rendered content
        waitForAnyElementContainingText("Markdown");

        // FileField - "file.txt"
        waitForAnyElementContainingText("file.txt");

        // BigDecimal - "99.99"
        waitForAnyElementContainingText("99.99");

        // VideoField - "video.mp4" (checking for text presence as VideoField often renders the filename or placeholder)
        // Adjust this if VideoField renders differently (e.g., checks <video> tag)
        waitForAnyElementContainingText("video.mp4");
    }
}
