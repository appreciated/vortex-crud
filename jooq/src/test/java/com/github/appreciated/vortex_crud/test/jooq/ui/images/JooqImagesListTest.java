package com.github.appreciated.vortex_crud.test.jooq.ui.images;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractImagesListTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "images_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqImagesListTest extends AbstractImagesListTest {
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
}
