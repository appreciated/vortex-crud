package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.checkbox_field;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JooqCheckboxFieldTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqCheckboxFieldTestApplication.class, args);
    }

}
