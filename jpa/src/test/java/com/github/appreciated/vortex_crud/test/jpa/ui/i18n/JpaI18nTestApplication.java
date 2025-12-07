package com.github.appreciated.vortex_crud.test.jpa.ui.i18n;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JpaI18nTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JpaI18nTestApplication.class, args);
    }
}
