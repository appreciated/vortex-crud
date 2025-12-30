package com.github.appreciated.vortex_crud.test.jpa.unit;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.jpa.service.JpaDataStoreFieldNameResolver;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@ColorScheme(ColorScheme.Value.DARK)
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
