package com.github.appreciated.vortex_crud.test.jooq.ui.missing_features;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMissingFeaturesActionsTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "missing_features_test.sql")
public class JooqMissingFeaturesActionsTest extends AbstractMissingFeaturesActionsTest {

    @Override
    protected String getPath() {
        return "missing-features-test-new";
    }
}
