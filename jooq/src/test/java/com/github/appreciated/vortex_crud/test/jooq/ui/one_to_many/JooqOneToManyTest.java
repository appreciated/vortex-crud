package com.github.appreciated.vortex_crud.test.jooq.ui.one_to_many;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractOneToManyTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "one_to_many_test.sql")
public class JooqOneToManyTest extends AbstractOneToManyTest {
}
