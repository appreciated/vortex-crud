package com.github.appreciated.vortex_crud.test.jooq.ui.single_component.single_component_route;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackageClasses = {
        JooqSingleComponentRouteVortexCrudConfiguration.class
    }
)
@Push
public class JooqSingleComponentRouteTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(JooqSingleComponentRouteTestApplication.class, args);
    }
}
