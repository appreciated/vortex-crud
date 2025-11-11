package com.github.appreciated.vortex_crud.test.jpa.ui.pdf;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractPdfTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "pdf_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaPdfTest extends AbstractPdfTest {

}
