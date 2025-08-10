package com.github.appreciated.vortex_crud.jpa.service.ui;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.jpa.service.JpaDataStoreFieldNameResolver;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.github.appreciated.vortex_crud.jpa.service",
    "com.github.appreciated.vortex_crud.core"
})
@Theme(variant = Lumo.DARK)
@Push
public class TestApplication implements AppShellConfigurator {

    @Bean
    public ReflectionService<String> reflectionService(JpaDataStoreFieldNameResolver fieldNameResolver) {
        return new ReflectionService<>(fieldNameResolver);
    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
