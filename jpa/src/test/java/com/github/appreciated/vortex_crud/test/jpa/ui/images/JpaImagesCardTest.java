package com.github.appreciated.vortex_crud.test.jpa.ui.images;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractImagesCardTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jpa/ui/images/images_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaImagesCardTest extends AbstractImagesCardTest {
    @Override
    protected String getPath() {
        return "images-grid";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "Red";
    }
}
