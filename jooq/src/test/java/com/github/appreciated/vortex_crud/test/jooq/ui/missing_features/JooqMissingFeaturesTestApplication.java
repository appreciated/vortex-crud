package com.github.appreciated.vortex_crud.test.jooq.ui.missing_features;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@ColorScheme(ColorScheme.Value.DARK)
@Push
@Import({JooqMissingFeaturesVortexCrudConfiguration.class})
public class JooqMissingFeaturesTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqMissingFeaturesTestApplication.class, args);
    }

}
