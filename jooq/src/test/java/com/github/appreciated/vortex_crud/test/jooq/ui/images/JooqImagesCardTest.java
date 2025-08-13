package com.github.appreciated.vortex_crud.test.jooq.ui.images;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractImagesCardTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "images_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqImagesCardTest extends AbstractImagesCardTest {
    @Override
    protected String getPath() {
        return "images-grid";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "Red";
    }
}
