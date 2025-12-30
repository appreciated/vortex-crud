package com.github.appreciated.vortex_crud.test.jooq.ui.custom_route;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@ColorScheme(ColorScheme.Value.DARK)
@Push
public class JooqCustomRouteTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqCustomRouteTestApplication.class, args);
    }

}
