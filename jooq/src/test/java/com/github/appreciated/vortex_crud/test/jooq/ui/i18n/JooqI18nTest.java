package com.github.appreciated.vortex_crud.test.jooq.ui.i18n;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractI18nSmokeTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("i18n_test.sql")
public class JooqI18nTest extends AbstractI18nSmokeTest {
}
