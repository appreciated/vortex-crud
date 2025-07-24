package com.github.appreciated.vortex_crud.uitest;

import com.github.appreciated.vortex_crud.uitest.pages.ProjectCardsTest;
import org.junit.jupiter.api.Disabled;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.context.annotation.Profile;

@Suite
@Profile("ui-tests")
@SelectClasses({ProjectCardsTest.class})
public class VortexCrudBaseUiTestSuite {
}

