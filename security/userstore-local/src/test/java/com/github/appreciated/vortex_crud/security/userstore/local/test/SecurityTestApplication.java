package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class SecurityTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(SecurityTestApplication.class, args);
    }
}
