package com.github.appreciated.vortex_crud.test.jooq.ui.custom_route;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JooqCustomRouteTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqCustomRouteTestApplication.class, args);
    }

}
