package com.github.appreciated.vortex_crud.test.jpa.ui.file;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFileTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "file_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaFileTest extends AbstractFileTest {

}
