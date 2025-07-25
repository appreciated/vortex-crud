package com.github.appreciated.vortex_crud.uitest;

import com.github.appreciated.vortex_crud.uitest.pages.ProjectCardsTest;
import com.github.appreciated.vortex_crud.uitest.pages.ProjectGridTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.context.annotation.Profile;

@Suite
@Profile("ui-tests")
@SelectClasses({ProjectCardsTest.class, ProjectGridTest.class})
public class VortexCrudBaseUiTestSuite {
}

