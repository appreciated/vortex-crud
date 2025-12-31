package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractNotificationPanelTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = JpaNotificationPanelTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:com/github/appreciated/vortex_crud/test/jpa/ui/notification_panel/notification_panel_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaNotificationPanelTest extends AbstractNotificationPanelTest {
}
