package com.github.appreciated.vortex_crud.test.jpa.ui.missing_features;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMissingFeaturesTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "missing_features_test.sql")
public class JpaMissingFeaturesTest extends AbstractMissingFeaturesTest {
}
