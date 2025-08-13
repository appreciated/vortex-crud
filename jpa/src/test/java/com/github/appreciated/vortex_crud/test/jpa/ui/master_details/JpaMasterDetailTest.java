package com.github.appreciated.vortex_crud.test.jpa.ui.master_details;

import com.github.appreciated.vortex_crud.uitest.tests.AbstractMasterDetailTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jpa/ui/kanban/kanban_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaMasterDetailTest extends AbstractMasterDetailTest {
    @Override
    protected String getPath() {
        return "tasks/done";
    }
}
