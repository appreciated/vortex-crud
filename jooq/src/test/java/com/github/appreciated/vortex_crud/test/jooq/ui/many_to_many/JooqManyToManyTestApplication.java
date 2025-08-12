package com.github.appreciated.vortex_crud.test.jooq.ui.many_to_many;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(variant = Lumo.DARK)
@Push
public class JooqManyToManyTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqManyToManyTestApplication.class, args);
    }

}
