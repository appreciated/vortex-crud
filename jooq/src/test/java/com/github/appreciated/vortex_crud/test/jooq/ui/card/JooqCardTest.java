package com.github.appreciated.vortex_crud.test.jooq.ui.card;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCardTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = "images_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqCardTest extends AbstractCardTest {
    @Override
    protected String getPath() {
        return "images-grid";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "Red";
    }

    @Test
    void testImagesDisplayed() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue());
        WebElement img = waitForElement(By.tagName("img"));
        assertTrue(img.isDisplayed());
    }
}
