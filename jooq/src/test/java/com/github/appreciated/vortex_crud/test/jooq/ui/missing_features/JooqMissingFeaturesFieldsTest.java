package com.github.appreciated.vortex_crud.test.jooq.ui.missing_features;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMissingFeaturesFieldsTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("missing_features_test.sql")
public class JooqMissingFeaturesFieldsTest extends AbstractMissingFeaturesFieldsTest {

    @Override
    protected String getPath() {
        return "missing-features-test-new";
    }
}
