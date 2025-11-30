package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.config.VortexCrudAutoConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.DefaultDialogFactoryRegistry;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = VortexCrudAutoConfiguration.class)
@ComponentScan(
        basePackages = {
                "com.github.appreciated.vortex_crud.security.userstore.local.test",
                "com.github.appreciated.vortex_crud.core.entity",
                "com.github.appreciated.vortex_crud.core.service",
                "com.github.appreciated.vortex_crud.security.core"
        },
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = DefaultDialogFactoryRegistry.class)
)
@Theme(variant = Lumo.DARK)
@Push
public class SecurityTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(SecurityTestApplication.class, args);
    }
}
