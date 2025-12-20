package com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel;

import com.github.appreciated.vortex_crud.test.jpa.ui.notification_panel.JpaNotificationPanelVortexCrudConfiguration;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Push
@EnableJpaRepositories(basePackageClasses = {JpaNotificationRepository.class})
@Import({JpaNotificationPanelVortexCrudConfiguration.class})
public class JpaNotificationPanelTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(JpaNotificationPanelTestApplication.class, args);
    }
}
