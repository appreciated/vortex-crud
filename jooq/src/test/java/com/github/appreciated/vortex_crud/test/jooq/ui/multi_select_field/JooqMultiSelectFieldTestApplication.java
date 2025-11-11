package com.github.appreciated.vortex_crud.test.jooq.ui.multi_select_field;

import com.github.appreciated.vortex_crud.core.spring.EnableVortexCrud;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableVortexCrud
@Theme(value = "vortex-crud", variant = Lumo.LIGHT)
public class JooqMultiSelectFieldTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqMultiSelectFieldTestApplication.class, args);
    }

}
