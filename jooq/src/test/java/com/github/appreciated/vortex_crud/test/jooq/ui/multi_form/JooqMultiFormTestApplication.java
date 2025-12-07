package com.github.appreciated.vortex_crud.test.jooq.ui.multi_form;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JooqMultiFormTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqMultiFormTestApplication.class, args);
    }
}
