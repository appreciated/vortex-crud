package com.github.appreciated.vortex_crud.test.jooq.ui.grid;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractGridTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jooq/ui/images/images_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqGridTest extends AbstractGridTest {
    @Override
    protected String getPath() {
        return "images-list";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "Red";
    }

    @Override
    protected String getFilterValuePresent() {
        return "Red";
    }

    @Override
    protected String getFilterValueAbsent() {
        return "Blue";
    }

    @Test
    void testImagesDisplayed() {
        navigateTo(getPath());
        waitForAnyElementContainingText(getExpectedVisibleValue());
        WebElement img = waitForElement(By.tagName("img"));
        assertTrue(img.isDisplayed());
    }
}
