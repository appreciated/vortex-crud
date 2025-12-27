package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.text_field;

import com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.JooqFieldValidationTestEnum;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JooqTextFieldTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqTextFieldTestApplication.class, args);
    }

}
