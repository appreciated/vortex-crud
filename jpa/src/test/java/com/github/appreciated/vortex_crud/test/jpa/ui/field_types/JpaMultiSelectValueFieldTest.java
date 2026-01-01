package com.github.appreciated.vortex_crud.test.jpa.ui.field_types;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMultiSelectValueFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql( "field_types_test.sql")
public class JpaMultiSelectValueFieldTest extends AbstractMultiSelectValueFieldTest {
}
