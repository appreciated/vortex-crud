package com.github.appreciated.vortex_crud.test.jooq.ui.many_to_many;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractManyToManyTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "many_to_many_test.sql")
public class JooqManyToManyTest extends AbstractManyToManyTest {
}
