package com.github.appreciated.vortex_crud.test.jooq.ui.notification_panel;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractNotificationPanelTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("notification_panel_test.sql")
public class JooqNotificationPanelTest extends AbstractNotificationPanelTest {
}
