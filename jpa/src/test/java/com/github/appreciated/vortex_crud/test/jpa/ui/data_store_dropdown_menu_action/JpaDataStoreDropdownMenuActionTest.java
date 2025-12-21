package com.github.appreciated.vortex_crud.test.jpa.ui.data_store_dropdown_menu_action;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractDataStoreDropdownMenuActionTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "data_store_dropdown_menu_action_test.sql")
public class JpaDataStoreDropdownMenuActionTest extends AbstractDataStoreDropdownMenuActionTest {
}
