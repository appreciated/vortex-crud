package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields.textarea;

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
public class JooqTextAreaTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqTextAreaTestApplication.class, args);
    }

}
