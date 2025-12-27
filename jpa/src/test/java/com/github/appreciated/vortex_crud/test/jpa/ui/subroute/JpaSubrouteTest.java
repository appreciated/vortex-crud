package com.github.appreciated.vortex_crud.test.jpa.ui.subroute;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSubrouteTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "subroute_test.sql")
public class JpaSubrouteTest extends AbstractSubrouteTest {

}
