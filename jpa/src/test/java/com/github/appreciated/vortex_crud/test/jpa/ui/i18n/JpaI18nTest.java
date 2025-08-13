package com.github.appreciated.vortex_crud.test.jpa.ui.i18n;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractI18nSmokeTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jpa/ui/i18n/i18n_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaI18nTest extends AbstractI18nSmokeTest {
    @Override
    protected String getPath() {
        return "projects-list";
    }

    @Override
    protected String getSampleDataText() {
        return "Project Alpha";
    }
}
