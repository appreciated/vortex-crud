package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractManyToManyFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql( "many_to_many_test.sql")
public class JpaManyToManyFieldTest extends AbstractManyToManyFieldTest {
}
