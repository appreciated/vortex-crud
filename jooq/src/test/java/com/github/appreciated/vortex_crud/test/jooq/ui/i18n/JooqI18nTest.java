package com.github.appreciated.vortex_crud.test.jooq.ui.i18n;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractI18nSmokeTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "i18n_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqI18nTest extends AbstractI18nSmokeTest {
}
