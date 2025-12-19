package com.github.appreciated.vortex_crud.test.jooq.ui.select_field;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JooqSelectFieldTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(JooqSelectFieldTestApplication.class, args);
    }
}
