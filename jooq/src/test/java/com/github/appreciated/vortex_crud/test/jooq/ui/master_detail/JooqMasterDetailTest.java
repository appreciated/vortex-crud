package com.github.appreciated.vortex_crud.test.jooq.ui.master_detail;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMasterDetailTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jooq/ui/master_detail/master_detail_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqMasterDetailTest extends AbstractMasterDetailTest {
    @Override
    protected String getPath() {
        return "tasks/done";
    }
}
