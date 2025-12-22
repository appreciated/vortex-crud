package com.github.appreciated.vortex_crud.test.jpa.ui.master_detail;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMasterDetailTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "master_detail_test.sql")
public class JpaMasterDetailTest extends AbstractMasterDetailTest {
}
