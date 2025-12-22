package com.github.appreciated.vortex_crud.test.jpa.ui.card;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCardTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "images_test.sql")
public class JpaCardTest extends AbstractCardTest {

}
