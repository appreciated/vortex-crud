package com.github.appreciated.vortex_crud.test.jooq.ui.card;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCardTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "images_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqCardTest extends AbstractCardTest {
}
