package com.github.appreciated.vortex_crud.test.jpa.ui.one_to_many;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractOneToManyFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql( "one_to_many_test.sql")
public class JpaOneToManyFieldTest extends AbstractOneToManyFieldTest {
}
