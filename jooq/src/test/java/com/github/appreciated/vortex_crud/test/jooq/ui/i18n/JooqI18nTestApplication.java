package com.github.appreciated.vortex_crud.test.jooq.ui.i18n;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JooqI18nTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqI18nTestApplication.class, args);
    }

}
