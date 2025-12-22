package com.github.appreciated.vortex_crud.test.jpa.ui.select_field;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaSelectFieldTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JpaSelectFieldTestApplication.class, args);
    }

}
