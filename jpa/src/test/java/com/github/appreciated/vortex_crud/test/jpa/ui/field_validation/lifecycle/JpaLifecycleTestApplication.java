package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.lifecycle;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JpaLifecycleTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JpaLifecycleTestApplication.class, args);
    }

}
