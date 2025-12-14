package com.github.appreciated.vortex_crud.test.jooq.ui.missing_features;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMissingFeaturesTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "missing_features_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqMissingFeaturesTest extends AbstractMissingFeaturesTest {

    @Override
    protected String getPath() {
        return "missing-features-test-new";
    }
}
