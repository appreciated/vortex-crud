package com.github.appreciated.vortex_crud.test.jpa.ui.single_form_route;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(variant = Lumo.DARK)
@Push
public class JpaSingleFormRouteTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JpaSingleFormRouteTestApplication.class, args);
    }

}
