package com.github.appreciated.vortex_crud.test.jpa.ui.field_types;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractPdfFieldTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "field_types_test.sql")
public class JpaPdfFieldTest extends AbstractPdfFieldTest {
}
