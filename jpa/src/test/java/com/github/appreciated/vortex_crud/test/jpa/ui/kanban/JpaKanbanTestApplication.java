package com.github.appreciated.vortex_crud.test.jpa.ui.kanban;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.appreciated.vortex_crud.test.jpa")
@Theme(variant = Lumo.DARK)
@Push
public class JpaKanbanTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JpaKanbanTestApplication.class, args);
    }
}
