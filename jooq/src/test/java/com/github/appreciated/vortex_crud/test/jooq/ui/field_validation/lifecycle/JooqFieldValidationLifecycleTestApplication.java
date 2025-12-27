package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.lifecycle;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JooqFieldValidationLifecycleTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqFieldValidationLifecycleTestApplication.class, args);
    }

}
