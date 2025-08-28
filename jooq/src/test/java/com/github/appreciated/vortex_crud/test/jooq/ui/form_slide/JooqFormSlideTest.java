package com.github.appreciated.vortex_crud.test.jooq.ui.form_slide;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFormSlideTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "images_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqFormSlideTest extends AbstractFormSlideTest {
}
