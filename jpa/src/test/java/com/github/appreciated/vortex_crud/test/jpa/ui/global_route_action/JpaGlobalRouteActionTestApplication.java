package com.github.appreciated.vortex_crud.test.jpa.ui.global_route_action;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JpaGlobalRouteActionTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JpaGlobalRouteActionTestApplication.class, args);
    }

}
