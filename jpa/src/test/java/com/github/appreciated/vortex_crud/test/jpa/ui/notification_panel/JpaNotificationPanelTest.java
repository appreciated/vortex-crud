package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractNotificationPanelTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "/notification_panel_test.sql")
public class JpaNotificationPanelTest extends AbstractNotificationPanelTest {
}
