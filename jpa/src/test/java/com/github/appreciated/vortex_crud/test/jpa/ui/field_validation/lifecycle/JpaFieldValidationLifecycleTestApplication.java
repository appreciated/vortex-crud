package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.lifecycle;

import com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JpaFieldValidationLifecycleTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JpaFieldValidationLifecycleTestApplication.class, args);
    }

}
