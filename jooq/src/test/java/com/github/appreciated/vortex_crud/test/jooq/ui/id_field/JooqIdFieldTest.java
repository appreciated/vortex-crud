package com.github.appreciated.vortex_crud.test.jooq.ui.id_field;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractIdFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "id_field_test.sql")
public class JooqIdFieldTest extends AbstractIdFieldTest {
    @Override
    protected String getPath() {
        return "id-test-list";
    }

    @Override
    protected String getItemName() {
        return "Test Entity 1";
    }
}
