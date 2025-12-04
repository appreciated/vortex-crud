package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.security.core.config.LocalStorageVortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.security.core.service.LocalStorageUserContextService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Theme(variant = Lumo.DARK)
@Push
@ComponentScan(basePackages = "com.github.appreciated.vortex_crud")
@Import({LocalStorageVortexCrudRbacPermissionChecker.class, LocalStorageUserContextService.class})
public class SecurityTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(SecurityTestApplication.class, args);
    }
}
